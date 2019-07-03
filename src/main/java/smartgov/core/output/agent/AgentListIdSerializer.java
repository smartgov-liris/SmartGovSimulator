package smartgov.core.output.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import smartgov.core.agent.core.Agent;

public class AgentListIdSerializer extends StdSerializer<List<? extends Agent<?>>> {

	private static final long serialVersionUID = 1L;

	public AgentListIdSerializer() {
		this(null);
	}
	protected AgentListIdSerializer(Class<List<? extends Agent<?>>> t) {
		super(t);
	}
	@Override
	public void serialize(List<? extends Agent<?>> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		List<String> agentIds = new ArrayList<>();
		for(Agent<?> agent : value) {
			agentIds.add(agent.getId());
		}
		gen.writeObject(agentIds);
		
	}

}
