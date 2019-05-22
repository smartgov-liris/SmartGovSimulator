package smartgov.core.agent.perception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Stores current perceptions in a Map of class names and a lists of objects from this class.
 * @author spageaud
 *
 */
public abstract class AbstractPerception {
	
	protected Map<String, List<?>> perceivedObjects;
	
	public AbstractPerception(){
		this.perceivedObjects = new HashMap<String, List<?>>();
	}
	
	public List<?> getPerceptionsForObject(String classname){
		return perceivedObjects.get(classname);
	}
	
	public void addPerceivedObjects(String className, List<?> objects){
		perceivedObjects.put(className, objects);
	}
	
	public Map<String, List<?>> getPerceivedObjects() {
		return perceivedObjects;
	}
	
	public List<String> getPerceivedName(){
		List<String> perceivedObjectClassName = new ArrayList<>();
		for(Entry<String, List<?>> entry : perceivedObjects.entrySet()){
			perceivedObjectClassName.add(entry.getKey());
		}
		return perceivedObjectClassName;
	}
	
	public void filterPerception(Perception perceptions){
		List<String> classNames = getPerceivedName();
		for(String className : classNames){
			if(perceptions.getObjectsPerceived().containsKey(className)){
				addPerceivedObjects(className, perceptions.getObjectsPerceived().get(className));
			}
		}
	}
	
	public void clear(){
		for(Entry<String, List<?>> perception : perceivedObjects.entrySet()){
			perceivedObjects.put(perception.getKey(), new ArrayList<>());
		}
	}
	
	@Override
	public String toString() {
		String temp = super.toString();
		for(Entry<String, List<?>> entry : perceivedObjects.entrySet()){
			temp += ", " + entry.getKey() + ": " + entry.getValue().size() + " items";
		}
		return temp += ".";
	}
	
}
