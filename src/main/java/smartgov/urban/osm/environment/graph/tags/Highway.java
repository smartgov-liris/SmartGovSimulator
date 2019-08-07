package smartgov.urban.osm.environment.graph.tags;

/**
 * Enumeration that describes main roads usable by cars in OSM.
 * 
 * For detailed information about each tag, check the official
 * <a href="https://wiki.openstreetmap.org/wiki/Key:highway">OSM documantation
 * </a>.
 * 
 *
 */
public enum Highway implements OsmTag {
	MOTORWAY("motorway"),
	TRUNK("trunk"),
	PRIMARY("primary"),
	SECONDARY("secondary"),
	TERTIARY("tertiary"),
	UNCLASSIFIED("unclassified"),
	RESIDENTIAL("residential"),
	MOTORWAY_LINK("motorway_link"),
	TRUNK_LINK("trunk_link"),
	PRIMARY_LINK("primary_link"),
	SECONDARY_LINK("secondary_link"),
	TERTIARY_LINK("tertiary_link"),
	SERVICE("service"),
	LIVING_STREET("living_street"),
	OTHER("");
	
	private String osmTag;
	
	private Highway(String osmTag) {
		this.osmTag = osmTag;
	}
	
	@Override
	public String getOsmTag() {
		return osmTag;
	}
	
	public static Highway fromOsmTag(String osmTag) {
		for(Highway highway : Highway.values()) {
			if(highway.getOsmTag().equals(osmTag)) {
				return highway;
			}
		}
		return Highway.OTHER;
	}
}
