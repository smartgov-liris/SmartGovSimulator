package smartgov.core.simulation.events;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import smartgov.core.simulation.time.Date;

public class CustomDateSerializer extends StdSerializer<Date>{

	private static final long serialVersionUID = 1L;

	public CustomDateSerializer() {
		this(null);
	}
	
	protected CustomDateSerializer(Class<Date> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("day", value.getDay());
		gen.writeStringField("weekDay", value.getWeekDay().toString());
		gen.writeStringField(
				"hour",
				value.getHour() + ":" + value.getMinutes() + ":" + value.getSeconds()
				);
		gen.writeEndObject();
	}

}
