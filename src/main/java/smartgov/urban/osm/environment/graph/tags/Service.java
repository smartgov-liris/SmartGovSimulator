package smartgov.urban.osm.environment.graph.tags;

public enum Service implements OsmTag {
	DRIVEWAY("driveway"),
	PARKING_AISLE("parking_aisle"),
	ALLEY("alley"),
	NONE("");

	private String tag;
	
	private Service(String osmTag) {
		this.tag = osmTag;
	}
	@Override
	public String getOsmTag() {
		// TODO Auto-generated method stub
		return tag;
	}

	public static Service fromOsmTag(String osmTag) {
		for(Service service : Service.values()) {
			if(service.getOsmTag().equals(osmTag)) {
				return service;
			}
		}
		return Service.NONE;
	}
}
