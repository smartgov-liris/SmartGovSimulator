package smartgov.core.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import smartgov.SmartGov;
import smartgov.core.agent.AbstractAgent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;

public class TestLoadWorldScenario {
	
	private SmartGov loadSmartGov() {
		return new SmartGov(new TestContext(this.getClass().getResource("test_scenario.properties").getFile()));
	}

	@Test
	public void loasSmartGovWithTestScenario() {
		SmartGov smartgov = loadSmartGov();
		
		assertThat(
				smartgov.getSimulationBuilder().getContext().getScenario(),
				notNullValue()
				);
		
		assertThat(
				smartgov.getSimulationBuilder().getContext().getScenario() instanceof TestScenario,
				equalTo(true)
				);
	}
	
	@Test
	public void testNodes() {
		SmartGov smartgov = loadSmartGov();
		
		assertThat(
				smartgov.getSimulationBuilder().getContext().nodes.keySet(),
				hasSize(4)
				);
		
		assertThat(
				smartgov.getSimulationBuilder().getContext().arcs.keySet(),
				hasSize(5)
				);
	}
	
	private class TestContext extends SmartGovContext {

		public TestContext(String configFile) {
			super(configFile);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Scenario loadScenario(String name) {
			return new TestScenario();
		}
	}
	
	private class TestScenario extends Scenario {
		
		private List<Node> nodes;
		private List<Arc> arcs;
		
		public TestScenario() {
			nodes = new ArrayList<>();
			Node node1 = new Node("1");
			Node node2 = new Node("2");
			Node node3 = new Node("3");
			Node node4 = new Node("4");
			nodes.add(node1);
			nodes.add(node2);
			nodes.add(node3);
			nodes.add(node4);
			
			arcs = new ArrayList<>();
			Arc arc1 = new Arc("1", node1, node2, 1);
			Arc arc2 = new Arc("2", node2, node3, 1);
			Arc arc3 = new Arc("3", node3, node4, 1);
			Arc arc4 = new Arc("4", node3, node1, 1);
			Arc arc5 = new Arc("5", node4, node4, 1);
			arcs.add(arc1);
			arcs.add(arc2);
			arcs.add(arc3);
			arcs.add(arc4);
			arcs.add(arc5);
		}

		@Override
		public Collection<Node> buildNodes() {
			return nodes;
		}

		@Override
		public Collection<Arc> buildArcs() {
			return arcs;
		}

		@Override
		public Collection<AbstractAgent<?>> buildAgents() {
			// TODO Auto-generated method stub
			return new ArrayList<>();
		}
		
	}

}
