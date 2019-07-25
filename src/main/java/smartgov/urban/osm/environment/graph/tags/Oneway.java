package smartgov.urban.osm.environment.graph.tags;

/**
 * Describe the <i>oneway</i> status of a road, according
 * to the official <a
 * href="https://wiki.openstreetmap.org/wiki/Key:oneway">OSM tag documentation</a>.
 * 
 */
public enum Oneway implements OsmTag {
	/**
	 * Corresponds to <i>oneway="no"</i>, or any other situation
	 * that does not correspond to <code>YES</code> or
	 * <code>REVERSED</code>. Default value when no <i>oneway</i>
	 * tag is specified.
	 */
	NO("no"),
	
	/**
	 * Corresponds to <i>highway="yes"</i>
	 */
	YES("yes"),
	
	/**
	 * Corresponds to <i>highway="-1"</i>
	 */
	REVERSED("-1");
	
	private String osmTag;
	
	private Oneway(String osmTag) {
		this.osmTag = osmTag;
	}

	@Override
	public String getOsmTag() {
		return osmTag;
	}
	
	public static Oneway fromOsmTag(String osmTag) {
		for(Oneway oneway : Oneway.values()) {
			if(oneway.getOsmTag().equals(osmTag))
				return oneway;
		}
		return null;
	}
	
	
}
