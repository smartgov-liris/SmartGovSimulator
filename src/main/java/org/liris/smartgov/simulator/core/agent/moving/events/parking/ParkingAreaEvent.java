package org.liris.smartgov.simulator.core.agent.moving.events.parking;

import org.liris.smartgov.simulator.core.agent.moving.ParkingArea;
import org.liris.smartgov.simulator.core.events.Event;

public abstract class ParkingAreaEvent extends Event {

	private ParkingArea parkingArea;
	
	public ParkingAreaEvent(ParkingArea parkingArea) {
		this.parkingArea = parkingArea;
	}
	
	public ParkingArea getParkingArea() {
		return parkingArea;
	}
}
