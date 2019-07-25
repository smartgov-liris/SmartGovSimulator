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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LatLon other = (LatLon) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		return true;
	}
	
	

	@Override
	public String toString() {
		return "(" + lat + ", " + lon + ")";
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
