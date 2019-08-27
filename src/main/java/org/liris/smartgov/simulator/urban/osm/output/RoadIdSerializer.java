package org.liris.smartgov.simulator.urban.osm.output;

import java.io.IOException;

import org.liris.smartgov.simulator.urban.osm.environment.graph.Road;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class RoadIdSerializer extends StdSerializer<Road> {

	private static final long serialVersionUID = 1L;

	public RoadIdSerializer() {
		this(null);
	}
	
	protected RoadIdSerializer(Class<Road> t) {
		super(t);
	}

	@Override
	public void serialize(Road value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.getId());
		
	}
	
	

}
