package smartgov.core.output.node;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import smartgov.core.environment.graph.node.Node;
/**
 * Custom jackson serializer used to represent arcs' target and start nodes.
 * Only the id of the nodes are serialized.
 *  
 * @author pbreugnot
 *
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
