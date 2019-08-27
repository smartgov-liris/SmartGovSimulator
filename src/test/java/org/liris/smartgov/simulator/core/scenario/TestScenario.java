package org.liris.smartgov.simulator.core.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.agent.moving.TestMovingAgent;
import org.liris.smartgov.simulator.core.agent.moving.TestMovingAgentBody;
import org.liris.smartgov.simulator.core.agent.moving.behavior.TestMovingBehavior;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.scenario.Scenario;

public class TestScenario extends Scenario {
	
	public static final String name = "TestScenario";

	@Override
	public Collection<Node> buildNodes(SmartGovContext context) {
		// Creating Nodes
		ArrayList<Node> testNodes = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			testNodes.add(new Node(String.valueOf(i + 1)));
		}
		
		return testNodes;
	}

	@Override
	public Collection<Arc> buildArcs(SmartGovContext context) {
		// Creating Arcs
		ArrayList<Arc> testArcs = new ArrayList<>();
		Arc arc1 = new Arc("1", context.nodes.get("1"), context.nodes.get("2"), 1);
		Arc arc2 = new Arc("2", context.nodes.get("2"), context.nodes.get("5"), 1);
		Arc arc3 = new Arc("3", context.nodes.get("2"), context.nodes.get("3"), 1);
		Arc arc4 = new Arc("4", context.nodes.get("5"), context.nodes.get("3"), 1);
		Arc arc5 = new Arc("5", context.nodes.get("3"), context.nodes.get("4"), 1);
		Arc arc6 = new Arc("6", context.nodes.get("4"), context.nodes.get("1"), 1);
		Arc arc7 = new Arc("7", context.nodes.get("1"), context.nodes.get("3"), 1);
		Arc arc8 = new Arc("8", context.nodes.get("3"), context.nodes.get("1"), 1);
		Arc arc9 = new Arc("9", context.nodes.get("2"), context.nodes.get("3"), 1);
		Arc arc10 = new Arc("10", context.nodes.get("3"), context.nodes.get("2"), 1);
		
		testArcs.addAll(Arrays.asList(arc1, arc2, arc3, arc4, arc5, arc6, arc7, arc8, arc9, arc10));
		return testArcs;
	}

	@Override
	public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
		// New body for the first shuttle
		TestMovingAgentBody shuttleBody = new TestMovingAgentBody();
		
		// The first shuttle will perform round trips between nodes 1 and 5.
		TestMovingAgent shuttle1 = new TestMovingAgent(
				"1",
				shuttleBody,
				new TestMovingBehavior(
						shuttleBody,
						context.nodes.get("1"),
						context.nodes.get("5"),
						context)
				);
		
		// New body for the second shuttle
		shuttleBody = new TestMovingAgentBody();
		
		// The first shuttle will perform round trips between nodes 2 and 4.
		TestMovingAgent shuttle2 = new TestMovingAgent(
				"2",
				shuttleBody,
				new TestMovingBehavior(
						shuttleBody,
						context.nodes.get("2"),
						context.nodes.get("4"),
						context)
				);
		
		return Arrays.asList(shuttle1, shuttle2);
	}
	
}
