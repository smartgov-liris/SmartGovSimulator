package smartgov.core.agent.moving.events.parking;

import smartgov.core.agent.moving.ParkingArea;

public class EnterParkingAreaEvent extends ParkingAreaEvent {

	public EnterParkingAreaEvent(ParkingArea parkingArea) {
		super(parkingArea);
	}

}
