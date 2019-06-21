package smartgov.urban.geo.environment.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.math.Vector2D;

import net.sf.javaml.core.kdtree.KDTree;
import smartgov.core.environment.graph.Arc;
import smartgov.core.environment.graph.Graph;
import smartgov.urban.geo.simulation.GISComputation;

/**
 * A Graph implementation used to represent a geographical graph, such as a
 * road graph.
 * 
 * @author pbreugnot
 */
public class GeoGraph extends Graph {

	private KDTree kdtree;
	
	/**
	 * GeoGraph constructor.
	 *
	 * @param nodes a GeoNode map
	 * @param arcs a GeoArc map
	 */
	public GeoGraph(Map<String, ? extends GeoNode> nodes, Map<String, ? extends GeoArc> arcs){
		super(nodes, Collections.unmodifiableMap(arcs));
		this.kdtree = new KDTree(2);
		for(GeoNode node : nodes.values()){
			this.kdtree.insert(node.getPositionAsArray(), node);
		}
	}
	
	/**
	 * Computes the nearest node from the specified coordinates.
	 *
	 * @param coordinate coordinates from which the search starts
	 * @return nearest node from specified coordinates
	 */
	public GeoNode getNearestNodeFrom(Coordinate coordinate){
		double[] coords = new double[2];
		coords[0] = coordinate.x;
		coords[1] = coordinate.y;
		return (GeoNode) this.kdtree.nearest(coords);
	}
	
	public Object[] getNearestNodesFrom(Coordinate coord, int numberOfNearestNodes){
		double[] coords = new double[2];
		coords[0] = coord.x;
		coords[1] = coord.y;
		return this.kdtree.nearest(coords, numberOfNearestNodes);
	}
	
	private Arc findNearestArcFromEdgeSpot(EdgeSpot edgeSpot, List<List<Arc>> arcs, double min){
		Arc arc = null;
		Coordinate coord = edgeSpot.getPosition();
		for(int j = 0; j < arcs.size(); j ++){
			List<Arc> currentList = arcs.get(j);
			for(int i = 0; i < currentList.size(); i++){
				Arc arcTemp = currentList.get(i);
				GeoNode startNode = (GeoNode) arcTemp.getStartNode();
				GeoNode targetNode = (GeoNode) arcTemp.getTargetNode();
				
				//*
				double X1 = startNode.getPosition().x;
				double X2 = targetNode.getPosition().x;
				double Y1 = startNode.getPosition().y;
				double Y2 = targetNode.getPosition().y;
				double XX = X2 - X1 ;
				double YY = Y2 - Y1 ;
				double X3 = coord.x;
				double Y3 = coord.y;
				double ShortestLength = ((XX * (X3 - X1)) + (YY * (Y3 - Y1))) / ((XX * XX) + (YY * YY)) ;
				
				//Projection of coordinate to edge
				double X4 = X1 + XX * ShortestLength ;
				double Y4 = Y1 + YY * ShortestLength ;
				Coordinate projectionToArc = new Coordinate(X4, Y4);
				//System.out.println("Projection: " + projectionToEdge.x + " " + projectionToEdge.y);
				Vector2D lineStart = new Vector2D(projectionToArc, startNode.getPosition());
				Vector2D lineEnd = new Vector2D(projectionToArc, targetNode.getPosition());
				Vector2D line = new Vector2D(startNode.getPosition(), targetNode.getPosition());
				//if(X4 < X2 && X4 > X1 && Y4 < Y2 && Y4 > Y1){
				if(lineStart.length() <= line.length() && lineEnd.length() <= line.length()){
					
					Vector2D v = new Vector2D(new Coordinate(X4, Y4), coord);
					
					//Check direction of the parking spot to the road
					if (v.getX() * line.getY() >= 0){	
						
						if(GISComputation.GPS2Meter(coord, projectionToArc) < min && GISComputation.GPS2Meter(coord, new Coordinate(X4,Y4)) < 15.){
							arc = arcTemp;//node.getIncomingEdges().get(i);	
							min = GISComputation.GPS2Meter(coord, projectionToArc);
							edgeSpot.setProjectionOnEdge(projectionToArc);
						}
					} else { //Check if the road is oneway or not
						boolean doNotExist = true;
						for(int k = 0; k < targetNode.getOutgoingArcs().size(); k++){
							if(targetNode.getOutgoingArcs().get(k).getTargetNode() == startNode){
								doNotExist = false;
								break;
							}
						}
						if(doNotExist){
							if(GISComputation.GPS2Meter(coord, projectionToArc) < min ){
								arc = arcTemp;//node.getIncomingEdges().get(i);	
								min = GISComputation.GPS2Meter(coord, projectionToArc);
								edgeSpot.setProjectionOnEdge(projectionToArc);
							}
						}
					}
				}
			}
		}
		return arc;
	}
	
	
	//http://gis.stackexchange.com/questions/11409/calculating-the-distance-between-a-point-and-a-virtual-line-of-two-lat-lngs
	private Arc findNearestArcFromEdgeSpotAroundNode(EdgeSpot spot, GeoNode node){
		double min = 15;
		
		List<List<Arc>> tempArcs = new ArrayList<>();
		tempArcs.add(node.getIncomingArcs());
		tempArcs.add(node.getOutgoingArcs());
		
		return findNearestArcFromEdgeSpot(spot, tempArcs, min);
	}
	
	/**
	 * Find the nearest Arc from the specified EdgeSpot, among the incoming and outgoing arcs
	 * of the specified nodes.
	 * 
	 * @param spot EdgeSpot of interest.
	 * @param nodes Nodes around which to search for the Arc.
	 * @return nearest Arc from spot among arcs of specified nodes.
	 */
	public Arc findNearestArcFromEdgeSpotAroundNodes(EdgeSpot spot, List<? extends GeoNode> nodes){
		Arc arc = null;
		Arc nearestArc=null;
		double distanceToArc=Double.MAX_VALUE;
		for(int i = 0; i < nodes.size(); i++){
			arc = findNearestArcFromEdgeSpotAroundNode(spot, nodes.get(i));
			if( arc!=null && GISComputation.GPS2Meter(spot.getPosition(),spot.getProjectionOnEdge())<distanceToArc)
				nearestArc =arc;
		}
		return nearestArc;
	}
	
	//http://stackoverflow.com/questions/1211212/how-to-calculate-an-angle-from-three-points
	public double computeAngle(Coordinate pointToKnow, Coordinate center, Coordinate otherNode){
		Vector2D ab = new Vector2D(pointToKnow, center);
		Vector2D ac = new Vector2D(otherNode, center);
		Vector2D bc = new Vector2D(pointToKnow, otherNode);
		double angleInRad = Math.acos((Math.pow(ab.length(),2) + Math.pow(ac.length(),2) - Math.pow(bc.length(),2))/(2*ab.length()*ac.length()));
		return angleInRad*180/Math.PI;
	}
}
