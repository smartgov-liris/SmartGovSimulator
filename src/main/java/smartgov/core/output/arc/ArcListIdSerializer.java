package smartgov.core.output.arc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import smartgov.core.environment.graph.arc.Arc;

/**
 * Jackson serializer used to write the outgoing and incoming arcs lists of nodes.
 * Even if those attribute are arc lists, we just serialize the lists has arrays of ids..
 * 
 * @author pbreugnot
 *
 */
public class ArcListIdSerializer extends StdSerializer<List<? extends Arc<?>>>{

	private static final long serialVersionUID = 1L;

	public ArcListIdSerializer() {
		this(null);
	}
	protected ArcListIdSerializer(Class<List<? extends Arc<?>>> t) {
		super(t);
	}


	@Override
	public void serialize(List<? extends Arc<?>> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		List<String> arcIds = new ArrayList<>();
		for(Arc<?> arc : value) {
			arcIds.add(arc.getId());
		}
		gen.writeObject(arcIds);
		
	}

}
