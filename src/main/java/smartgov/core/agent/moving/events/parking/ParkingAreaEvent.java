package smartgov.core.agent.moving.events.parking;

import smartgov.core.agent.moving.ParkingArea;
import smartgov.core.events.Event;

public abstract class ParkingAreaEvent extends Event {

	private ParkingArea parkingArea;
	
	public ParkingAreaEvent(ParkingArea parkingArea) {
		this.parkingArea = parkingArea;
	}
	
	public ParkingArea getParkingArea() {
		return parkingArea;
	}
}
