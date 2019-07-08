package smartgov.core.simulation.time;

/**
 * Action handler used to trigger a given action at a given date.
 * 
 *
 */
public class DelayedActionHandler implements Comparable<DelayedActionHandler>{
	
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
	
	/**
	 * DelayedActionHandlers are sorted according to their dates.
	 * @param arg0 delayedActionHandler to compare
	 * @return 0, -1 or +1 respectively if this handler should be trigger
	 * at the same time, before of after the specified one.
	 */
	@Override
	public int compareTo(DelayedActionHandler arg0) {
		return this.getDate().compareTo(arg0.getDate());
	}
}
