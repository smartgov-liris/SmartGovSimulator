package smartgov.urban.geo.environment.graph;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.environment.graph.node.Node;

/**
 * Generic abstract class to represent a Node on a map.
 * 
 * @see Node
 * @author pbreugnot
 *
 * @param <Tarc> GeoArc type
 */
public abstract class GeoNode<Tarc extends GeoArc<?>> extends Node<Tarc> {
	
	private Coordinate position;
	
	public GeoNode(String id, Coordinate position){
		super(id);
		this.position = position;
	}
	
	public GeoNode(String id, Coordinate position, List<Tarc> outgoingArcs, List<Tarc> incomingArcs) {
		this(id, position);
		this.outgoingArcs = outgoingArcs;
		this.incomingArcs = incomingArcs;
	}
	
	public GeoNode(String id, double latitude, double longitude, List<Tarc> outgoingArcs, List<Tarc> incomingArcs){
		this(id,  new Coordinate(latitude, longitude), outgoingArcs, incomingArcs);
	}
	
	public Coordinate getPosition() {
		return position;
	}
	
	public double[] getPositionAsArray(){
		double[] coords = new double[2];
		coords[0] = position.x;
		coords[1] = position.y;
		return coords;
	}
}
