package smartgov.core.simulation;

import java.util.HashMap;
import java.util.Map;

public class HumanIndicator {

	private String name;
	private Map<String, Double> coefficients; //Store min, max, weight, slope and plug
	private Map<String, String> attributes; //Store mode, type, categories and text
	
	public HumanIndicator(String name){
		this.name = name;
		coefficients = new HashMap<>();
		attributes   = new HashMap<>();
	}
	
	public HumanIndicator(String name, Map<String, Double> coefficients){
		this(name);
		this.coefficients = coefficients;
	}
	
	public HumanIndicator(String name, Map<String, Double> coefficients, Map<String, String> attributes){
		this(name, coefficients);
		this.attributes = attributes;
	}
	
	public Map<String, Double> getCoefficients() {
		return coefficients;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public String getName() {
		return name;
	}
	
	public String getText(){
		return attributes.get("TEXT");
	}
	
	public String getMode(){
		return attributes.get("MODE");
	}
	
	public String getType(){
		return attributes.get("TYPE");
	}
	
	public String getCategorie(){
		return attributes.get("CATEGORIE");
	}
	
}
