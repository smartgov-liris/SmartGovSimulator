package smartgov.urban.geo.agent.event;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.events.Event;

public class GeoMoveEvent extends Event {
	
	private Coordinate oldCoordinate;
	private Coordinate newCoordinate;
	private double distanceCrossed;

	public GeoMoveEvent(
			Coordinate oldCoordinate,
			Coordinate newCoordinate,
			double distanceCrossed) {
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
