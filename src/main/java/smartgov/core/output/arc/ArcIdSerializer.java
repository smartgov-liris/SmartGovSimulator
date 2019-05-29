package smartgov.core.output.arc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import smartgov.core.environment.graph.arc.Arc;

public class ArcIdSerializer extends StdSerializer<Arc<?>> {

	private static final long serialVersionUID = 1L;

	public ArcIdSerializer() {
		this(null);
	}
	
	protected ArcIdSerializer(Class<Arc<?>> t) {
		super(t);
	}

	@Override
	public void serialize(Arc<?> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		gen.writeString(value.getId());
	}
	

}
