package smartgov.core.simulation.time;

public enum WeekDay {
	MONDAY(0),
	TUESDAY(1),
	WEDNESDAY(2),
	THURSDAY(3),
	FRIDAY(4),
	SATURDAY(5),
	SUNDAY(6);
	
	private static WeekDay[] valuesByOffset = {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};
	private int offset;
	
	private WeekDay(int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public WeekDay after(int dayCount) {
		int offset = (getOffset() + dayCount) % 7;
		return valuesByOffset[offset];
	}
}
