package smartgov.urban.osm.agent.mover;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.core.agent.moving.events.MoveEvent;
import smartgov.core.events.EventHandler;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.osm.environment.graph.TestOsmContext;

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
	}
	
	@Test
	public void testMaxSpeedReach() throws InterruptedException {
		SmartGov smartGov = loadCarMoverTestScenario();
		
		MaxSpeed maxSpeed = new MaxSpeed();
		
		GeoAgentBody leaderAgent = (GeoAgentBody) smartGov.getContext().agents.get("2").getBody();
		
		leaderAgent.addOnMoveListener(new EventHandler<MoveEvent>(){

				@Override
				public void handle(MoveEvent event) {
					System.out.println(leaderAgent.getSpeed());
					if (leaderAgent.getSpeed() > maxSpeed.getValue()) {
						maxSpeed.setValue(leaderAgent.getSpeed());
					}
					
				}
				
			});
		
		SmartGov.getRuntime().start(1000);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		assertThat(
				maxSpeed.getValue(),
				greaterThan(0.0)
				);
		
//		assertThat(
//				maxSpeed.getValue(),
//				lessThanOrEqualTo(CarMoverTestScenario.leaderMaxSpeed)
//				);
//		
//		assertThat(
//				maxSpeed.getValue(),
//				equalTo(CarMoverTestScenario.leaderMaxSpeed)
//				);
	}
	
	private class MaxSpeed {
		
		private Double value = -Double.MAX_VALUE;

		public Double getValue() {
			return value;
		}

		public void setValue(Double value) {
			this.value = value;
		}
	}

}
