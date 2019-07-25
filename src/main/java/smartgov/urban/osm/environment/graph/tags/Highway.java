package smartgov.urban.osm.environment.graph.tags;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

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
	
	/**
	 * Custom jackson serializer used to serialize an Highway using the
	 * corresponding OSM tag.
	 *
	 */
	public static class HighwaySerializer extends StdSerializer<Highway> {
		
		private static final long serialVersionUID = 1L;

		public HighwaySerializer() {
			this(null);
		}
		
		protected HighwaySerializer(Class<Highway> t) {
			super(t);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void serialize(Highway value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeObject(value.getOsmTag());
		}
		
	}
}
