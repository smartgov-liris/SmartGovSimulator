package smartgov.urban.osm.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import smartgov.urban.osm.environment.graph.Road.OneWay;

public class OneWayDeserializer extends StdDeserializer<OneWay> {

	private static final long serialVersionUID = 1L;

	public OneWayDeserializer() {
		this(null);
	}
	
	protected OneWayDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public OneWay deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		Iterator<String> keyIterator = node.fieldNames();
		while(keyIterator.hasNext()) {
			String key = keyIterator.next();
			if(key.equals("oneway")) {
				String oneway = node.get("oneway").asText();
				if (oneway.equals("-1")) {
					/* According to the OSM documentation (https://wiki.openstreetmap.org/wiki/Key:oneway),
					 * This is supposed to be a very rare case. It means that
					 * the current way is one-way, be in the opposite order of its node.
					 */
					return OneWay.REVERSED;
				}
				else if (oneway.equals("yes")) {
					return OneWay.YES;
				}
				/*
				 * Other values are considered as not one-way ("no", "reversible", "alternating",...)
				 */
				return OneWay.NO;
			}
		}

		// If no oneway tag is present
		return OneWay.NO;
	}

}
