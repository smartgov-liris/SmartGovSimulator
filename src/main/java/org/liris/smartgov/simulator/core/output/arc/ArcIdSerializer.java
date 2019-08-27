package org.liris.smartgov.simulator.core.output.arc;

import java.io.IOException;

import org.liris.smartgov.simulator.core.environment.graph.Arc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Custom Jackson serializer that can be used to serialize an Arc
 * using only its id.
 */
public class ArcIdSerializer extends StdSerializer<Arc> {

	private static final long serialVersionUID = 1L;

	public ArcIdSerializer() {
		this(null);
	}
	
	protected ArcIdSerializer(Class<Arc> t) {
		super(t);
	}

	@Override
	public void serialize(Arc value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		gen.writeString(value.getId());
	}
	

}
