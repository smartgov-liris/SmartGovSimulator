package smartgov.urban.osm.agent.mover;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.events.DestinationReachedEvent;
import smartgov.core.agent.moving.events.MoveEvent;
import smartgov.core.agent.moving.events.NodeReachedEvent;
import smartgov.core.events.EventHandler;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.environment.TestOsmContext;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.Road;

public class CarMoverTest {
	
	public static SmartGov loadCarMoverTestScenario() {
		return new SmartGov(new TestOsmContext(CarMoverTest.class.getResource("car_mover_test.properties").getFile()));
	}
	
	@Test
	public void testLoadEnvironment() {
		SmartGov smartGov = loadCarMoverTestScenario();
		
		assertThat(
				smartGov.getContext().nodes.values(),
				hasSize(4)
				);
		
		assertThat(
				smartGov.getContext().arcs.values(),
				hasSize(4)
				);
		
		for (Agent<?> agent : smartGov.getContext().agents.values()) {
			assertThat(
					((GeoAgentBody) agent.getBody()).getMover() instanceof CarMover,
					equalTo(true)
					);
			
		}
	}
	
	@Test
	public void testMaxSpeedReach() throws InterruptedException {
		SmartGov smartGov = loadCarMoverTestScenario();
		
		MaxBean maxSpeed = new MaxBean();
		
		GeoAgentBody leaderAgent = (GeoAgentBody) smartGov.getContext().agents.get("2").getBody();
		
		leaderAgent.addOnMoveListener(new EventHandler<MoveEvent>(){

				@Override
				public void handle(MoveEvent event) {
					maxSpeed.update(leaderAgent.getSpeed());
				}
				
			});
		
		SmartGov.getRuntime().start(100);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		assertThat(
				maxSpeed.getValue(),
				greaterThan(0.0)
				);
		
		assertThat(
				Math.abs(maxSpeed.getValue() - CarMoverTestScenario.leaderMaxSpeed),
				lessThanOrEqualTo(0.1)
				);
	}
	
	@Test
	public void testFollowerStayAfterLeader() throws InterruptedException {
		SmartGov smartGov = loadCarMoverTestScenario();
		
		MaxBean followerMaxSpeed = new MaxBean();
		MinBean minDistanceBetweenAgents = new MinBean();
		
		GeoAgentBody followerAgent = (GeoAgentBody) smartGov.getContext().agents.get("1").getBody();
		GeoAgentBody leaderAgent = (GeoAgentBody) smartGov.getContext().agents.get("2").getBody();
		
		leaderAgent.addOnNodeReachedListener(new EventHandler<NodeReachedEvent>() {
			@Override
			public void handle(NodeReachedEvent event) {
				// TODO : Bug here, when the leader reaches a node,
				// it probably disappear from the road what causes
				// a follower acceleration, and the distance goes bellow
				// the legal limit.
				System.out.println(
						"Node reached, distance between agents : " +
						GISComputation.GPS2Meter(followerAgent.getPosition(), leaderAgent.getPosition())
						);
				
			}
			
		});
		
		followerAgent.addOnMoveListener(new EventHandler<MoveEvent>(){

				@Override
				public void handle(MoveEvent event) {
					followerMaxSpeed.update(followerAgent.getSpeed());

					Double currentDistance = GISComputation.GPS2Meter(followerAgent.getPosition(), leaderAgent.getPosition());
					minDistanceBetweenAgents.update(
							currentDistance
							);

					System.out.println(currentDistance);
					
					Road followerRoad = ((OsmArc) followerAgent.getPlan().getCurrentArc()).getRoad();
					Road leaderRoad = ((OsmArc) leaderAgent.getPlan().getCurrentArc()).getRoad();
					
					assertThat(
							followerRoad,
							equalTo(leaderRoad)
							);
					
					assertThat(
							followerRoad.leaderOfAgent(followerAgent),
							equalTo(leaderAgent)
							);
				}
				
			});
		
		SmartGov.getRuntime().start(500);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		assertThat(
				CarMoverTestScenario.followerMaxSpeed,
				greaterThan(CarMoverTestScenario.leaderMaxSpeed)
				);
		
		assertThat(
				minDistanceBetweenAgents.getValue(),
				greaterThanOrEqualTo(CarMoverTestScenario.vehicleSize)
				);
	}
	
	@Test
	public void maxAccelerationsAreRespected() throws InterruptedException {
		SmartGov smartGov = loadCarMoverTestScenario();
		
		GeoAgentBody leaderAgent = (GeoAgentBody) smartGov.getContext().agents.get("2").getBody();
		MaxBean leaderMaxAcceleration = new MaxBean();
		MinBean leaderMinBraking = new MinBean();
		ValueBean leaderLastSpeed = new ValueBean(leaderAgent.getSpeed());
		
		GeoAgentBody followerAgent = (GeoAgentBody) smartGov.getContext().agents.get("1").getBody();
		MaxBean followerMaxAcceleration = new MaxBean();
		MinBean followerMinBraking = new MinBean();
		ValueBean followerLastSpeed = new ValueBean(followerAgent.getSpeed());
		
		leaderAgent.addOnMoveListener(new EventHandler<MoveEvent>(){

				@Override
				public void handle(MoveEvent event) {
					Double acceleration = (leaderAgent.getSpeed() - leaderLastSpeed.getValue()) / SmartGov.getRuntime().getTickDuration();
					System.out.println(acceleration);
					if (acceleration >= 0) {
						leaderMaxAcceleration.update(acceleration);
					}
					else {
						leaderMinBraking.update(acceleration);
					}
					leaderLastSpeed.setValue(leaderAgent.getSpeed());
				}
				
			});
		
		followerAgent.addOnMoveListener(new EventHandler<MoveEvent>(){

			@Override
			public void handle(MoveEvent event) {
				Double acceleration = (followerAgent.getSpeed() - followerLastSpeed.getValue()) / SmartGov.getRuntime().getTickDuration();
				
				if (acceleration >= 0) {
					followerMaxAcceleration.update(acceleration);
				}
				else {
					followerMinBraking.update(acceleration);
				}
				followerLastSpeed.setValue(followerAgent.getSpeed());
			}
			
		});
		
		SmartGov.getRuntime().start(100);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		/*
		 * Acceleration
		 */
		assertThat(
				leaderMaxAcceleration.getValue(),
				lessThan(CarMoverTestScenario.maximumAcceleration)
				);
		
		// Leader should reach its maximum acceleration (computing from the Gipps' model, with a 0.1 threshold)
		assertThat(
				leaderMaxAcceleration.getValue(),
				greaterThan(
						2.5*CarMoverTestScenario.maximumAcceleration*CarMover.reactionTime*Math.sqrt(0.025) - 0.1
						)
				);
		
		assertThat(
				followerMaxAcceleration.getValue(),
				lessThan(CarMoverTestScenario.maximumAcceleration)
				);
		
		/*
		 * Braking
		 */
		assertThat(
				leaderMinBraking.getValue(),
				greaterThan(CarMoverTestScenario.maximumBraking)
				);
		
		assertThat(
				followerMinBraking.getValue(),
				greaterThan(CarMoverTestScenario.maximumBraking)
				);
	}
	
	private class ValueBean {
		
		private Double value;
		
		public ValueBean(Double defaultValue) {
			this.value = defaultValue;
		}
		
		public void setValue(double value) {
			this.value = value;
		}

		public Double getValue() {
			return value;
		}
		
	}
	
	private class MaxBean extends ValueBean {
		
		public MaxBean() {
			super(-Double.MAX_VALUE);
		}
		
		public void update(Double value) {
			if(value > getValue()) {
				setValue(value);
			}
		}
	}
	
	private class MinBean extends ValueBean {
		
		public MinBean() {
			super(Double.MAX_VALUE);
		}
		
		public void update(Double value) {
			if(value < getValue()) {
				setValue(value);
			}
		}
	}

}
