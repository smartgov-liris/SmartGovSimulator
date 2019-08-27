package org.liris.smartgov.simulator.urban.osm.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OsmLoader<T extends Object> {

	public List<T> loadOsmElements(File jsonFile, Class<T> classToLoad) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType type = objectMapper.getTypeFactory().
				  constructCollectionType(List.class, classToLoad);
		return objectMapper.readValue(jsonFile, type);
	}

}
