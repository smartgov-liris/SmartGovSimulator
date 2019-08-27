package org.liris.smartgov.simulator.urban.geo.environment.graph;

import java.util.Map;

import org.liris.smartgov.simulator.urban.geo.utils.lonLat.LonLat;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.strtree.ItemBoundable;
import org.locationtech.jts.index.strtree.ItemDistance;
import org.locationtech.jts.index.strtree.STRtree;

/**
 * An
 * <a href="https://locationtech.github.io/jts/javadoc/org/locationtech/jts/index/strtree/STRtree.html">
 * JTS STRTree</a> (Sort-Tile-Recursive algorithm) wrapper used to compute proximity relations among geo nodes.
 * 
 * Distances used are Euclidian distances computed from geographical coordinates projected using the
 * Equirectangular (or Plate Carrée) projection.
 * 
 * @author pbreugnot
 */
public class GeoStrTree {

	private Map<String, ? extends GeoNode> nodes;
	private STRtree strtree;
	
	/**
	 * Builds an STRTree from the specified nodes. Notice
	 * that the specified node map does not necessarily contains
	 * all the nodes of the simulation. However, nearestNeighbours
	 * will obviously be found among the specified nodes.
	 *
	 * @param nodes a GeoNode map
	 */
	public GeoStrTree(Map<String, ? extends GeoNode> nodes){
		this.nodes = nodes;
		this.strtree = new STRtree(nodes.size());
		for(GeoNode node : nodes.values()){
			Coordinate projectedPoint = new LonLat().project(node.getPosition());
			this.strtree.insert(
					new Envelope(projectedPoint),
					node);
		}
	}
	
	/**
	 * GeoNode contained in this GeoStrTree.
	 * 
	 * @return nodes of this KdTree
	 */
	public Map<String, ? extends GeoNode> getNodes() {
		return nodes;
	}



	/**
	 * Computes the nearest node from the specified coordinates.
	 *
	 * @param coordinate coordinates from which the search starts
	 * @return nearest node from specified coordinates
	 */
	public GeoNode getNearestNodeFrom(Coordinate coordinate){
		return (GeoNode) this.strtree.nearestNeighbour(
				new Envelope(coordinate),
				null,
				new GeoNodeDistance());
	}
	
	public GeoNode[] getNearestNodesFrom(Coordinate coord, int numberOfNearestNodes){
		return (GeoNode[]) this.strtree.nearestNeighbour(
				new Envelope(coord),
				null,
				new GeoNodeDistance(),
				numberOfNearestNodes
				);
	}
	
	/*
	 * Item distance implementation that uses Euclidian distance on projected geo coordinates.
	 * See the JTS javadoc for more information.
	 */
	private static class GeoNodeDistance implements ItemDistance {

		@Override
		public double distance(ItemBoundable item1, ItemBoundable item2) {
			return ((Envelope) item1.getBounds()).centre().distance(((Envelope) item2.getBounds()).centre());
		}
		
	}

	/*
	 * THIS IS COMPLETELY UNTESTED
	 * Has been quickly adapted so that it compiles, and left there for a future usage.
	 */
//	private Arc findNearestArcFromEdgeSpot(EdgeSpot edgeSpot, List<List<Arc>> arcs, double min){
//		Arc arc = null;
//		Coordinate coord = new LonLat().project(edgeSpot.getPosition());
//		for(int j = 0; j < arcs.size(); j ++){
//			List<Arc> currentList = arcs.get(j);
//			for(int i = 0; i < currentList.size(); i++){
//				Arc arcTemp = currentList.get(i);
//				GeoNode startNode = (GeoNode) arcTemp.getStartNode();
//				GeoNode targetNode = (GeoNode) arcTemp.getTargetNode();
//				
//				//*
//				double X1 = startNode.getPosition().lon;
//				double X2 = targetNode.getPosition().lon;
//				double Y1 = startNode.getPosition().lat;
//				double Y2 = targetNode.getPosition().lat;
//				double XX = X2 - X1 ;
//				double YY = Y2 - Y1 ;
//				double X3 = coord.x;
//				double Y3 = coord.y;
//				double ShortestLength = ((XX * (X3 - X1)) + (YY * (Y3 - Y1))) / ((XX * XX) + (YY * YY)) ;
//				
//				//Projection of coordinate to edge
//				double X4 = X1 + XX * ShortestLength ;
//				double Y4 = Y1 + YY * ShortestLength ;
//				Coordinate projectionToArc = new Coordinate(X4, Y4);
//				//System.out.println("Projection: " + projectionToEdge.x + " " + projectionToEdge.y);
//				Vector2D lineStart = new Vector2D(
//						projectionToArc,
//						new LonLat().project(startNode.getPosition())
//						);
//				Vector2D lineEnd = new Vector2D(
//						projectionToArc,
//						new LonLat().project(targetNode.getPosition())
//						);
//				Vector2D line = new Vector2D(
//						new LonLat().project(startNode.getPosition()),
//						new LonLat().project(targetNode.getPosition())
//						);
//				//if(X4 < X2 && X4 > X1 && Y4 < Y2 && Y4 > Y1){
//				if(lineStart.length() <= line.length() && lineEnd.length() <= line.length()){
//					
//					Vector2D v = new Vector2D(new Coordinate(X4, Y4), coord);
//					
//					//Check direction of the parking spot to the road
//					if (v.getX() * line.getY() >= 0){	
//						
//						if(LatLon.distance(
//								new LonLat().unproject(coord), new LonLat().unproject(projectionToArc)
//								) < min
//								&& LatLon.distance(
//										new LonLat().unproject(coord),
//										new LonLat().unproject(new Coordinate(X4,Y4))) < 15.){
//							arc = arcTemp;//node.getIncomingEdges().get(i);	
//							min = LatLon.distance(new LonLat().unproject(coord), new LonLat().unproject(projectionToArc));
//							edgeSpot.setProjectionOnEdge(new LonLat().unproject(projectionToArc));
//						}
//					} else { //Check if the road is oneway or not
//						boolean doNotExist = true;
//						for(int k = 0; k < targetNode.getOutgoingArcs().size(); k++){
//							if(targetNode.getOutgoingArcs().get(k).getTargetNode() == startNode){
//								doNotExist = false;
//								break;
//							}
//						}
//						if(doNotExist){
//							if(LatLon.distance(new LonLat().unproject(coord), new LonLat().unproject(projectionToArc)) < min ){
//								arc = arcTemp;//node.getIncomingEdges().get(i);	
//								min = LatLon.distance(new LonLat().unproject(coord), new LonLat().unproject(projectionToArc));
//								edgeSpot.setProjectionOnEdge(new LonLat().unproject(projectionToArc));
//							}
//						}
//					}
//				}
//			}
//		}
//		return arc;
//	}
//	
//	/*
//	 * THIS IS COMPLETELY UNTESTED
//	 * As been quickly adapted so that it compiles, and left there for a future usage.
//	 */
//	//http://gis.stackexchange.com/questions/11409/calculating-the-distance-between-a-point-and-a-virtual-line-of-two-lat-lngs
//	private Arc findNearestArcFromEdgeSpotAroundNode(EdgeSpot spot, GeoNode node){
//		double min = 15;
//		
//		List<List<Arc>> tempArcs = new ArrayList<>();
//		tempArcs.add(node.getIncomingArcs());
//		tempArcs.add(node.getOutgoingArcs());
//		
//		return findNearestArcFromEdgeSpot(spot, tempArcs, min);
//	}
//	
//	/**
//	 * Find the nearest Arc from the specified EdgeSpot, among the incoming and outgoing arcs
//	 * of the specified nodes.
//	 * 
//	 * @deprecated
//	 * THIS IS COMPLETELY UNTESTED
//	 * As been quickly adapted so that it compiles, and left there for a future usage.
//	 * 
//	 * @param spot EdgeSpot of interest.
//	 * @param nodes Nodes around which to search for the Arc.
//	 * @return nearest Arc from spot among arcs of specified nodes.
//	 */
//	public Arc findNearestArcFromEdgeSpotAroundNodes(EdgeSpot spot, List<? extends GeoNode> nodes){
//		Arc arc = null;
//		Arc nearestArc=null;
//		double distanceToArc=Double.MAX_VALUE;
//		for(int i = 0; i < nodes.size(); i++){
//			arc = findNearestArcFromEdgeSpotAroundNode(spot, nodes.get(i));
//			if( arc!=null && LatLon.distance(spot.getPosition(),spot.getProjectionOnEdge())<distanceToArc)
//				nearestArc =arc;
//		}
//		return nearestArc;
//	}
	
//	//http://stackoverflow.com/questions/1211212/how-to-calculate-an-angle-from-three-points
//	public double computeAngle(Coordinate pointToKnow, Coordinate center, Coordinate otherNode){
//		Vector2D ab = new Vector2D(pointToKnow, center);
//		Vector2D ac = new Vector2D(otherNode, center);
//		Vector2D bc = new Vector2D(pointToKnow, otherNode);
//		double angleInRad = Math.acos((Math.pow(ab.length(),2) + Math.pow(ac.length(),2) - Math.pow(bc.length(),2))/(2*ab.length()*ac.length()));
//		return angleInRad*180/Math.PI;
//	}
}
