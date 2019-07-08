package smartgov.urban.geo.scenario;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.agent.core.Agent;
import smartgov.core.agent.moving.behavior.TestMovingBehavior;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.scenario.Scenario;
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
						context));
		agentBody.setPosition(((GeoNode) context.nodes.get("1")).getPosition());
		
		return Arrays.asList(geoAgent);
	}
	
}
