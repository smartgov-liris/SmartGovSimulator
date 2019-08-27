package org.liris.smartgov.simulator.core.output.node;

import java.io.IOException;

import org.liris.smartgov.simulator.core.environment.graph.Node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Custom Jackson serializer that can be used to serialize a Node
 * using only its id.
 */
public class NodeIdSerializer extends StdSerializer<Node>{

	private static final long serialVersionUID = 1L;

	public NodeIdSerializer(){
		this(null);
	}
	
	protected NodeIdSerializer(Class<Node> t) {
		super(t);
	}

	@Override
	public void serialize(Node value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.getId());
	}
	
	

}
