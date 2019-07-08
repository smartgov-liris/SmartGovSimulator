package smartgov.core.scenario;

import java.util.Collection;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import smartgov.core.environment.TestContext;
import smartgov.core.environment.TestSmartGovContext;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Node;
import smartgov.core.scenario.Scenario;

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
