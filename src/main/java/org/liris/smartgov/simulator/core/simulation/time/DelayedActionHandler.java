package org.liris.smartgov.simulator.core.simulation.time;

/**
 * Action handler used to trigger a given action at a given date.
 * 
 *
 */
public class DelayedActionHandler {
	
	private Date date;
	private DelayedAction action;
	
	/**
	 * DelayedActionHandler constructor.
	 * 
	 * @param date date at which the action should be triggered
	 * @param action action to trigger
	 */
	public DelayedActionHandler(Date date, DelayedAction action) {
		this.date = date;
		this.action = action;
	}
	
	/**
	 * Date at which the action should be triggered
	 * @return date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Action to trigger at the given date.
	 * @return action
	 */
	public DelayedAction getAction() {
		return action;
	}
}
