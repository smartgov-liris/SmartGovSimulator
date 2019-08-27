package org.liris.smartgov.simulator.urban.geo.environment.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.liris.smartgov.simulator.urban.geo.environment.graph.GeoKdTree;
import org.liris.smartgov.simulator.urban.geo.environment.graph.GeoNode;
import org.liris.smartgov.simulator.urban.geo.utils.LatLon;
import org.liris.smartgov.simulator.urban.geo.utils.lonLat.LonLat;
import org.locationtech.jts.geom.Coordinate;

public class GeoKdTreeTest {

	private static GeoKdTree buildTestGraph() {
		Map<String, GeoNode> nodes = new HashMap<>();
		nodes.put("1", new GeoNode("1", new LatLon(45.7829296, 4.8680849)));
		nodes.put("2", new GeoNode("2", new LatLon(45.7822063, 4.8648563)));
		nodes.put("3", new GeoNode("3", new LatLon(45.7842329, 4.865034)));
		nodes.put("4", new GeoNode("4", new LatLon(45.7844936, 4.8673742)));
		
		return new GeoKdTree(nodes);
	}
	
	@Test
	public void findClosestNodeTest() {
		GeoKdTree testGraph = buildTestGraph();
		Coordinate point = new LonLat().project(new LatLon(45.782475, 4.865148));
		
		GeoNode closestNode  = testGraph.getNearestNodeFrom(point);
		
		assertThat(
				closestNode,
				equalTo((GeoNode) testGraph.getNodes().get("2"))
				);
	}
}
