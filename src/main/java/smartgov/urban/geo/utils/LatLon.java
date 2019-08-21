package smartgov.urban.geo.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Class used to represent geographical coordinates.
 *
 */
@JsonSerialize(using = LatLon.Serializer.class)
public class LatLon {

	/**
	 * Latitude in degree
	 */
	public double lat;
	/**
	 * Longitude in degree
	 */
	public double lon;

	/**
	 * LatLon constructor.
	 * 
	 * @param lat latitude
	 * @param lon longitude
	 */
	public LatLon(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * Computes the distance in meter from point 1 to point 2 using the Haversine formula.
	 * 
	 * <p>
	 * Alias for {@link Projection#haversineDistance(LatLon, LatLon)}.
	 * </p>
	 * 
	 * @param point1 first geographical point
	 * @param point2 second geographical point
	 * @return distance in meter
	 */
	public static double distance(LatLon point1, LatLon point2) {
		return Projection.haversineDistance(point1, point2);
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
