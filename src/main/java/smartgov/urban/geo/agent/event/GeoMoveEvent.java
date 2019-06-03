package smartgov.urban.geo.agent.event;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.agent.events.MoveEvent;
import smartgov.core.environment.graph.arc.Arc;
import smartgov.core.environment.graph.node.Node;

public class GeoMoveEvent extends MoveEvent {
	
	private Coordinate oldCoordinate;
	private Coordinate newCoordinate;
	private double distanceCrossed;

	public GeoMoveEvent(
			Coordinate oldCoordinate,
			Coordinate newCoordinate,
			Arc oldArc,
			Arc newArc,
			Node oldNode,
			Node newNode,
			double distanceCrossed) {
		super(oldArc, newArc, oldNode, newNode);
		this.oldCoordinate = oldCoordinate;
		this.newCoordinate = newCoordinate;
		this.distanceCrossed = distanceCrossed;
	}

	public Coordinate getOldCoordinate() {
		return oldCoordinate;
	}

	public Coordinate getNewCoordinate() {
		return newCoordinate;
	}
	
	public double getDistanceCrossed() {
		return distanceCrossed;
	}
	
	

}
