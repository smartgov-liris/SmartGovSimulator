package smartgov.urban.geo.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonSerialize(using = LatLon.Serializer.class)
public class LatLon {

	public double lat;
	public double lon;

	public LatLon(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}
	
	public static double distance(LatLon point1, LatLon point2) {
		return Projection.haversineDistance(point1.lat, point2.lat, point1.lon, point2.lon);
	}
	

	public static class Serializer extends StdSerializer<LatLon> {

		private static final long serialVersionUID = 1L;

		public Serializer() {
			this(null);
		}

		protected Serializer(Class<LatLon> t) {
			super(t);
		}

		@Override
		public void serialize(LatLon value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			double coordinates[] = {value.lat, value.lon};
			gen.writeObject(coordinates);
		}
		
	}
}
