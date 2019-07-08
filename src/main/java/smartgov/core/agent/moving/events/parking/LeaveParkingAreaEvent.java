package smartgov.core.agent.moving.events.parking;

import smartgov.core.agent.moving.ParkingArea;

public class LeaveParkingAreaEvent extends ParkingAreaEvent {

	public LeaveParkingAreaEvent(ParkingArea parkingArea) {
		super(parkingArea);
	}

}
