package smartgov.urban.osm.environment.graph.tags;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Interface that enumerations corresponding to OSM tags
 * can implement.
 *
 */
public interface OsmTag {

	/**
	 * Osm representation of the key of the tag.
	 * 
	 * @return osm tag
	 */
	public String getOsmTag();
	
	
	/**
	 * Custom jackson serializer used to serialize an osm tag using
	 * the {@link #getOsmTag()} value.
	 *
	 */
	public static class OsmTagSerializer extends StdSerializer<OsmTag> {
		
		private static final long serialVersionUID = 1L;

		public OsmTagSerializer() {
			this(null);
		}
		
		protected OsmTagSerializer(Class<OsmTag> t) {
			super(t);
		}

		@Override
		public void serialize(OsmTag value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeObject(value.getOsmTag());
		}
		
	}
}
