package org.liris.smartgov.simulator.testUtils;

public class EventChecker {

	private Boolean triggered = false;
	
	public void check() {
		triggered = true;
	}
	
	public Boolean hasBeenTriggered() {
		return triggered;
	}
}
