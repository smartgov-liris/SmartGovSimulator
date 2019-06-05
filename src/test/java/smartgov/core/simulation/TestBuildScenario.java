package smartgov.core.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.TestContext;
import smartgov.core.environment.TestSmartGovContext;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;

public class TestBuildScenario {

	@Test
	public void testBuildNodes() {
		Scenario testScenario = new TestScenario();
		TestContext context = TestSmartGovContext.loadTestContext();
		
		Collection<Node> nodes = testScenario.buildNodes(context);
		
		assertThat(
				nodes,
				hasSize(5)
		);
	}
	
	@Test
	public void testBuildArcs() {
		Scenario testScenario = new TestScenario();
		TestContext context = TestSmartGovContext.loadTestContext();
		
		Collection<Node> nodes = testScenario.buildNodes(context);
		for(Node node : nodes) {
			context.nodes.put(node.getId(), node);
		}
		
		Collection<Arc> arcs = testScenario.buildArcs(context);
		
		assertThat(
				arcs,
				hasSize(10)
				);
	}

}
