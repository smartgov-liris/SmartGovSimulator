package smartgov.models.lez.copert.tableParser;

import smartgov.models.lez.copert.fields.CopertField;
import smartgov.models.lez.copert.fields.EuroNorm;
import smartgov.models.lez.copert.fields.Fuel;
import smartgov.models.lez.copert.fields.RandomField;
import smartgov.models.lez.copert.fields.VehicleCategory;

public enum CopertHeader {
	CATEGORY ("Category"),
	FUEL ("Fuel"),
	SEGMENT ("Segment"),
	EURO_STANDARD ("Euro Standard"),
	TECHNOLOGY ("Technology"),
	POLLUTANT ("Pollutant"),
	MODE ("Mode"),
	ROAD_SLOPE ("Road Slope"),
	LOAD ("Load"),
	MIN_SPEED ("Min Speed [km/h]"),
	MAX_SPEED ("Max Speed [km/h]"),
	ALPHA ("Alpha"),
	BETA ("Beta"),
	GAMMA ("Gamma"),
	DELTA ("Delta"),
	EPSILON ("Epsilon"),
	ZITA ("Zita"),
	HTA ("Hta"),
	THITA ("Thita"),
	REDUCTION_FACTOR ("Reduction Factor [%]"),
	BIO_REDUCTION_FACTOR ("Bio Reduction Factor [%]"),
	NULL (""); // Actually, there are column with no headers at the end of Copert tables.
	
	private final String columnName;
	
	private CopertHeader(String columnName) {
		this.columnName = columnName;
	}
	
	public String columnName() {
		return columnName;
	}
	
	public static CopertField randomField(CopertHeader header) {
		switch(header) {
		case CATEGORY:
			return VehicleCategory.RANDOM;
		case FUEL:
			return Fuel.RANDOM;
		case SEGMENT:
			return RandomField.RANDOM;
		case EURO_STANDARD:
			return EuroNorm.RANDOM;
		default:
			return null;
		}
	}
	
	public static CopertHeader getValue(String string) {
		for(CopertHeader value : values()){
			if (value.columnName().equals(string)) {
				return value;
			}
		}
		return null;
	}
	
}
