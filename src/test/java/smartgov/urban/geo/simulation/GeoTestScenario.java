package smartgov.urban.geo.simulation;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.locationtech.jts.geom.Coordinate;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.behavior.TestMovingBehavior;
import smartgov.core.agent.moving.events.NodeReachedEvent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.TestContext;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.events.EventHandler;
import smartgov.core.simulation.Scenario;
import smartgov.urban.geo.SmartGovGeoTest;
import smartgov.urban.geo.agent.GeoAgent;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.mover.BasicGeoMover;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;

public class GeoTestScenario extends Scenario {
	
	public static final String name = "geoTest";

	@Override
	public Collection<GeoNode> buildNodes(SmartGovContext context) {
		ArrayList<GeoNode> nodes = new ArrayList<>();
		
		nodes.add(
			new GeoNode(
					"1",
					new Coordinate(4.8680849, 45.7829296)
					)
			);
		nodes.add(
				new GeoNode(
					"2",
					new Coordinate(4.8648563, 45.7822063)
					)
				);
		nodes.add(
				new GeoNode(
					"3",
					new Coordinate(4.865034, 45.7842329)
					)
				);
		nodes.add(
				new GeoNode(
					"4",
					new Coordinate(4.8673742, 45.7844936)
					)
				);
		
		return nodes;
	}

	@Override
	public Collection<GeoArc> buildArcs(SmartGovContext context) {
		ArrayList<GeoArc> arcs = new ArrayList<>();
		
		arcs.add(
				new GeoArc(
						"1",
						(GeoNode) context.nodes.get("1"),
						(GeoNode) context.nodes.get("2")
						)
					);
		
		arcs.add(
				new GeoArc(
						"2",
						(GeoNode) context.nodes.get("2"),
						(GeoNode) context.nodes.get("3")
						)
					);
		
		arcs.add(
				new GeoArc(
						"3",
						(GeoNode) context.nodes.get("3"),
						(GeoNode) context.nodes.get("4")
						)
					);
		
		arcs.add(
				new GeoArc(
						"4",
						(GeoNode) context.nodes.get("4"),
						(GeoNode) context.nodes.get("1")
						)
					);
		
		return arcs;
	}

	@Override
	public Collection<Agent<?>> buildAgents(SmartGovContext context) {
		BasicGeoMover mover = new BasicGeoMover();
		GeoAgentBody agentBody = new GeoAgentBody(mover);
		mover.setAgentBody(agentBody);
		
		agentBody.setSpeed(1);

		GeoAgent geoAgent = new GeoAgent(
				"1",
				agentBody,
				new TestMovingBehavior(
						agentBody,
						context.nodes.get("1"),
						context.nodes.get("3"),
						(TestContext) context));
		agentBody.setPosition(((GeoNode) context.nodes.get("1")).getPosition());
		
		return Arrays.asList(geoAgent);
	}
	
	@Test
	public void testLoadScenario() {
		SmartGovGeoTest smartGov = new SmartGovGeoTest();
		
		assertThat(
				smartGov.getContext().nodes.values(),
				hasSize(4)
				);
		
		assertThat(
				smartGov.getContext().arcs.values(),
				hasSize(4)
				);
		
		for (Node node : smartGov.getContext().nodes.values()) {
			assertThat(
					node instanceof GeoNode,
					equalTo(true)
					);
		}
		
		for (Arc arc : smartGov.getContext().arcs.values()) {
			assertThat(
					arc instanceof GeoArc,
					equalTo(true)
					);
		}
	}

	@Test
	public void testArcLength() {
		SmartGovGeoTest smartGov = new SmartGovGeoTest();
		
		for (Arc arc : smartGov.getContext().arcs.values()) {
			assertThat(
					arc.getLength(),
					equalTo(GISComputation.GPS2Meter(
							((GeoNode) arc.getStartNode()).getPosition(),
							((GeoNode) arc.getTargetNode()).getPosition()
							)
							)
					);
		}
	}
	
	@Test
	public void testAgentIsMoving() throws InterruptedException {
		SmartGovGeoTest smartGov = new SmartGovGeoTest();
		
		ArrayList<String> nodeCrossedIds = new ArrayList<>();
		
		((MovingAgentBody) smartGov.getContext().agents.get("1").getBody())
			.addOnNodeReachedListener(new EventHandler<NodeReachedEvent>() {

				@Override
				public void handle(NodeReachedEvent event) {
					nodeCrossedIds.add(event.getNode().getId());
					
				}
				
			});
		
		double roundLength = 0;
		for(Arc arc : smartGov.getContext().arcs.values()) {
			roundLength += ((GeoArc) arc).getLength();
		}
		
		double roundTimeInSeconds = roundLength / 1; // speed = 1 m/s for now
		
		int roundTimeInTicks = (int) Math.floor(roundTimeInSeconds / SmartGov.getRuntime().getTickDuration()) + 1;
		
		
		
		// We don't know the exact required time that the agent need to cross
		// the square, so we will just check that he has crossed all the nodes
		// in the correct order.
		SmartGovGeoTest.getRuntime().start(2 * roundTimeInTicks);
		
		while(SmartGovGeoTest.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(100);
		}
		
		SmartGov.logger.info("Node crossed : " + nodeCrossedIds);
		
		assertThat(
				nodeCrossedIds,
				equalTo(Arrays.asList("2", "3", "3", "4", "1", "1", "2", "3", "3", "4", "1", "1"))
				);
	}
	
	
}
