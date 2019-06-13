package smartgov.core.output.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import smartgov.core.environment.graph.Node;

/**
 * Custom Jackson serializer that can be used to serialize a Node list as the
 * corresponding list of ids.
 *
 * @author pbreugnot
 */
public class NodeListIdSerializer extends StdSerializer<List<? extends Node>>{

	private static final long serialVersionUID = 1L;

	public NodeListIdSerializer() {
		this(null);
	}
	
	protected NodeListIdSerializer(Class<List<? extends Node>> t) {
		super(t);
	}

	@Override
	public void serialize(List<? extends Node> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		List<String> nodeIds = new ArrayList<>();
		for(Node node : value) {
			nodeIds.add(node.getId());
		}
		gen.writeObject(nodeIds);
	}

}
