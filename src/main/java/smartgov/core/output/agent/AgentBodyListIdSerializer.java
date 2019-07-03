package smartgov.core.output.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import smartgov.core.agent.core.AgentBody;

public class AgentBodyListIdSerializer extends StdSerializer<List<? extends AgentBody<?>>> {

	private static final long serialVersionUID = 1L;

	public AgentBodyListIdSerializer() {
		this(null);
	}
	protected AgentBodyListIdSerializer(Class<List<? extends AgentBody<?>>> t) {
		super(t);
	}
	@Override
	public void serialize(List<? extends AgentBody<?>> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		List<String> agentIds = new ArrayList<>();
		for(AgentBody<?> body : value) {
			agentIds.add(body.getAgent().getId());
		}
		gen.writeObject(agentIds);
		
	}

}
