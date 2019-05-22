package smartgov.core.agent.perception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Perception stores a map of class names and objects perceived in the environment.
 * @author spageaud
 *
 */
public class Perception {

	private Map<String, List<Object>> objectsPerceived;
	
	public Perception(){
		objectsPerceived = new HashMap<String, List<Object>>();
	}
	
	public Perception(Object object){
		objectsPerceived = new HashMap<String, List<Object>>();
		addPerceivedObject(object);
	}
	
	public void addPerceivedObject(Object object){
		if(object instanceof Perception){
			//Combine to Perception objects together
			Perception perceptObject = (Perception)object;
			List<String> classNames = perceptObject.getClassName();
			for(int nameIndex = 0; nameIndex < classNames.size(); nameIndex ++){
				List<?> perceptions = perceptObject.getObjectsPerceived().get(classNames.get(nameIndex));
				if(perceptions != null){					
					for(int itemIndex = 0; itemIndex < perceptions.size(); itemIndex ++){
						addPerceivedObject(perceptions.get(itemIndex));
					}
				}
			}
		} else {
			if(!objectsPerceived.containsKey(object.getClass().getSimpleName())){
				List<Object> newList = new ArrayList<>();
				objectsPerceived.put(object.getClass().getSimpleName(), newList);
			}
			addValue(object);
		}
	}
	
	@SuppressWarnings("unused")
	private void addKey(Object object){
		if(!objectsPerceived.containsKey(object.getClass().getSimpleName())){
			List<Object> newList = new ArrayList<>();
			objectsPerceived.put(object.getClass().getSimpleName(), newList);
		}
	}
	
	private void addValue(Object object){
		List<Object> list = objectsPerceived.get(object.getClass().getSimpleName());
		if(!list.contains(object)){
			list.add(object);
			objectsPerceived.put(object.getClass().getSimpleName(), list);
		}
	}
	
	public Map<String, List<Object>> getObjectsPerceived() {
		return objectsPerceived;
	}
	
	/**
	 * Get the list of objectsPerceived
	 * @return list of class names currently in objectsPerceived
	 */
	public List<String> getClassName(){
		List<String> perceivedObjectClassName = new ArrayList<>();
		for(Entry<String, List<Object>> entry : objectsPerceived.entrySet()){
			perceivedObjectClassName.add(entry.getKey());
		}
		return perceivedObjectClassName;
	}

}
