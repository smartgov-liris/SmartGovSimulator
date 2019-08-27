package org.liris.smartgov.simulator.core.output.coordinate;

import java.io.IOException;

import org.locationtech.jts.geom.Coordinate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Custom Jackson serializer used to serialize a Coordinate as an xy array.
 */
public class CoordinateSerializer extends StdSerializer<Coordinate> {

	private static final long serialVersionUID = 1L;

	public CoordinateSerializer() {
		this(null);
	}
	
	protected CoordinateSerializer(Class<Coordinate> t) {
		super(t);
	}

	@Override
	public void serialize(Coordinate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		double[] coordinates = { value.x, value.y };
		gen.writeObject(coordinates);
	}

}
