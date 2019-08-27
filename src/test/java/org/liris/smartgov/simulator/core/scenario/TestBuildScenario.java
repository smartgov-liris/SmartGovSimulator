package org.liris.smartgov.simulator.core.scenario;

import java.util.Collection;

import org.junit.Test;
import org.liris.smartgov.simulator.core.environment.TestContext;
import org.liris.smartgov.simulator.core.environment.TestSmartGovContext;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.scenario.Scenario;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class TestBuildScenario {

	@Test
	public void testBuildNodes() {
		Scenario testScenario = new TestScenario();
		TestContext context = TestSmartGovContext.loadTestContext();
		
		Collection<? extends Node> nodes = testScenario.buildNodes(context);
		
		assertThat(
				nodes,
				hasSize(5)
		);
	}
	
	@Test
	public void testBuildArcs() {
		Scenario testScenario = new TestScenario();
		TestContext context = TestSmartGovContext.loadTestContext();
		
		Collection<? extends Node> nodes = testScenario.buildNodes(context);
		for(Node node : nodes) {
			context.nodes.put(node.getId(), node);
		}
		
		Collection<? extends Arc> arcs = testScenario.buildArcs(context);
		
		assertThat(
				arcs,
				hasSize(10)
				);
	}

}
