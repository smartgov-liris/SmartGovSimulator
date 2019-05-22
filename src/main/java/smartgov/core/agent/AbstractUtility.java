package smartgov.core.agent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import smartgov.core.agent.properties.AbstractProperties;
import smartgov.core.simulation.HumanIndicator;

public abstract class AbstractUtility<P extends AbstractProperties> {

	protected static String MINIMUM = "min";
	protected static String MAXIMUM = "max";
	protected static String WEIGHT = "weight";
	protected static String COEFA = "normCoefA";
	protected static String COEFB = "normCoefB";
	protected static String TYPE = "TYPE";
	protected static String TEXT = "TEXT";
	protected static String CATEGORIE = "CATEGORIE";
	protected static String MODE = "MODE";
	
	protected Map<String, Map<String, Double>> indicatorsCoefs;
	protected Map<String, Double> areaCoefs;
	protected Map<String, HumanIndicator> indicators;
	
	protected String profileName;
	
	public AbstractUtility(Map<String, String> jsonFiles, String profile){
		indicatorsCoefs = new HashMap<>();
		areaCoefs = new HashMap<>();
		readAreaCoefsFromJson(null, profile);
		indicators = new HashMap<>();
		for(Entry<String, String> file : jsonFiles.entrySet()){
			readJson(file.getValue(), profile, file.getKey());
		}
	}
	
	public AbstractUtility(Map<String, String> jsonFiles, String profile, String areaCoefFile){
		indicatorsCoefs = new HashMap<>();
		areaCoefs = new HashMap<>();
		readAreaCoefsFromJson(areaCoefFile, profile);
		indicators = new HashMap<>();
		for(Entry<String, String> file : jsonFiles.entrySet()){
			readJson(file.getValue(), profile, file.getKey());
		}
		profileName = profile;
	}
	
	protected double getScoreFor(String indicatorName, double value){
		Map<String, Double> values = indicatorsCoefs.get(indicatorName);
		double score = values.get(COEFA)*value + values.get(COEFB); //Linear function
		if(score > 1){
			return 1;
		} else if(score < 0){
			return 0;
		} else {
			return score;
		}
	}
	
	public abstract double getScore(Object objectToScore, P properties);
	
	public double getScore(String mode, Object objectToScore, P properties){
		double normalIndScoreSum = 0.0;
		double normalIndCoefSum = 0.0;
		double bonusIndScoreSum = 0.0;
		double bonusIndCoefSum = 0.0;
		for(Entry<String, HumanIndicator> indicator : indicators.entrySet()){
			if(indicator.getValue().getMode().equals(mode)){
				if(indicator.getValue().getType().equals("normal")){
					normalIndScoreSum += getScoreFor(indicator.getKey(), parseCategorieFromIndicator(objectToScore, properties, indicator.getValue())) * getValuesOf(indicator.getKey()).get(WEIGHT);
					normalIndCoefSum += getValuesOf(indicator.getKey()).get(WEIGHT);
				} else if (indicator.getValue().getType().equals("bonus")){
					bonusIndScoreSum += getScoreFor(indicator.getKey(), parseCategorieFromIndicator(objectToScore, properties, indicator.getValue())) * getValuesOf(indicator.getKey()).get(WEIGHT);
					bonusIndCoefSum += getValuesOf(indicator.getKey()).get(WEIGHT);
				}
			}
		}
		return Double.min((normalIndScoreSum / normalIndCoefSum) + (bonusIndScoreSum / (normalIndCoefSum + bonusIndCoefSum)), 1.0);
	}
	
	public double getScore(String mode, Object objectToScore, P properties, String tag){
		double normalIndScoreSum = 0.0;
		double normalIndCoefSum = 0.0;
		double bonusIndScoreSum = 0.0;
		double bonusIndCoefSum = 0.0;
		if(tag == null || tag.equals("")) {
			tag = "Default";
		}
		for(Entry<String, HumanIndicator> indicator : indicators.entrySet()){
			if(indicator.getValue().getMode().equals(mode)){
				if(indicator.getValue().getType().equals("normal")){
					normalIndScoreSum += getScoreFor(indicator.getKey(), parseCategorieFromIndicator(objectToScore, properties, indicator.getValue())) * getValuesOf(indicator.getKey()).get(WEIGHT) * areaCoefs.get(tag);
					normalIndCoefSum += getValuesOf(indicator.getKey()).get(WEIGHT);
				} else if (indicator.getValue().getType().equals("bonus")){
					bonusIndScoreSum += getScoreFor(indicator.getKey(), parseCategorieFromIndicator(objectToScore, properties, indicator.getValue())) * getValuesOf(indicator.getKey()).get(WEIGHT) * areaCoefs.get(tag);
					bonusIndCoefSum += getValuesOf(indicator.getKey()).get(WEIGHT);
				}
			}
		}
		return Double.min((normalIndScoreSum / normalIndCoefSum) + (bonusIndScoreSum / (normalIndCoefSum + bonusIndCoefSum)), 1.0);
	}
	
	public abstract double getSatisfaction(Object objectToScore, P properties);
	
	public double getSatisfaction(String mode, Object objectToScore, P properties){
		double normalIndScoreSum = 0.0;
		double normalIndCoefSum = 0.0;
		double bonusIndScoreSum = 0.0;
		double bonusIndCoefSum = 0.0;
		for(Entry<String, HumanIndicator> indicator : indicators.entrySet()){
			if(indicator.getValue().getMode().equals(mode)){
				if(indicator.getValue().getType().equals("normal")){
					normalIndScoreSum += getScoreFor(indicator.getKey(), parseCategorieFromIndicator(objectToScore, properties, indicator.getValue())) * getValuesOf(indicator.getKey()).get(WEIGHT);
					normalIndCoefSum += getValuesOf(indicator.getKey()).get(WEIGHT);
				} else if (indicator.getValue().getType().equals("bonus")){
					bonusIndScoreSum += getScoreFor(indicator.getKey(), parseCategorieFromIndicator(objectToScore, properties, indicator.getValue())) * getValuesOf(indicator.getKey()).get(WEIGHT);
					bonusIndCoefSum += getValuesOf(indicator.getKey()).get(WEIGHT);
				}
			}
		}
		return Double.max((normalIndScoreSum / normalIndCoefSum) - (bonusIndScoreSum / (normalIndCoefSum + bonusIndCoefSum)), 0.0);
	}
	
	protected double getNormaliseValueBetween(double minimum, double maximum, double value){
		return (value - minimum)/(maximum - minimum);
	}
	
	protected double normCoefA(double minimum, double maximum, double nominalValue, double maxValue){
		return (maximum - minimum)/(nominalValue - maxValue);
	}
	
	protected double normCoefB(double minimum, double nominalValue, double maxValue){
		return (maxValue - minimum)/(nominalValue - maxValue);
	}
	
	protected double slope(double highUtilityBound, double lowUtilityBound){
		if(highUtilityBound == lowUtilityBound){
			return 1;
		}
		return (1/(highUtilityBound-lowUtilityBound));
	}
	
	protected double plug(double slope, double highUtilityBound){
		return (1-slope*highUtilityBound);
	}
	
	protected void readJson(String jsonFile, String profile, String indicatorName){
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			JsonNode rootNode = objectMapper.readTree(new File(jsonFile));
			
			JsonNode profileNode = rootNode.get(profile);
			JsonNode infoNode = rootNode.get("Info");
			
			Map<String, Double> values = new HashMap<>();
			values.put(MINIMUM, infoNode.get("MIN").asDouble());
			values.put(MAXIMUM, infoNode.get("MAX").asDouble());
			values.put(WEIGHT, profileNode.get("weight").asDouble());
			
			Map<String, String> attributes = new HashMap<>();
			attributes.put(TYPE, infoNode.get(TYPE).asText());
			attributes.put(TEXT, infoNode.get(TEXT).asText());
			attributes.put(MODE, infoNode.get(MODE).asText());
			attributes.put(CATEGORIE, infoNode.get(CATEGORIE).asText());
			
			JsonNode intervalsJson = profileNode.get("intervals");
			int index = 0;
			List<Double> bornesList = new ArrayList<>();
			double maxUtility = Double.MIN_VALUE;
			double minUtility = Double.MAX_VALUE;
			int indexOfMaxUtility = -1;
			int indexOfMinUtility = -1;
			while(true){
				JsonNode currentInterval = intervalsJson.get(String.valueOf(index));
				if(currentInterval == null){
					break;
				}
				bornesList.add(currentInterval.get("borne").asDouble());
				if(maxUtility < currentInterval.get("utility").asDouble()){
					maxUtility = currentInterval.get("utility").asDouble();
					indexOfMaxUtility = index;
				}
				if(minUtility > currentInterval.get("utility").asDouble()){
					minUtility = currentInterval.get("utility").asDouble();
					indexOfMinUtility = index;
				}
				index++;
			}
			if(bornesList.size() == 1){
				values.put(COEFA, slope(bornesList.get(0), bornesList.get(0)));
				values.put(COEFB, plug(values.get(COEFA), bornesList.get(0)));
			} else {
				values.put(COEFA, slope(bornesList.get(indexOfMaxUtility), bornesList.get(indexOfMinUtility)));
				values.put(COEFB, plug(values.get(COEFA), bornesList.get(indexOfMaxUtility)));
			}
			indicatorsCoefs.put(indicatorName, values);
			indicators.put(indicatorName, new HumanIndicator(indicatorName, values, attributes) );
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Read specific weights for tagged zone in the environment
	 * @param jsonFile Coefficient for tagged zone, if no file is provided the coefficients are set to default
	 * @param profile
	 */
	protected void readAreaCoefsFromJson(String jsonFile, String profile) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		if(jsonFile == null) {
			areaCoefs.put("Default", 1.0);
		} else {
			try{
				JsonNode rootNode = objectMapper.readTree(new File(jsonFile));
				Iterator<String> keySet = rootNode.fieldNames();
				while(keySet.hasNext()) {
					String areaID = keySet.next();
					JsonNode currentArea = rootNode.get(areaID);
					if(currentArea == null) {
						break;
					}
					parseArea(currentArea, profile, areaID);
				}
			} catch (Exception e){
				areaCoefs.put("Default", 1.0);
				e.printStackTrace();
			}
		}
	}
	
	private void parseArea(JsonNode currentArea, String profile, String areaID) {
		JsonNode currentProfile = currentArea.get(profile);
		areaCoefs.put(areaID, currentProfile.get("weight").asDouble());
	}
	
	public String getProfileName() {
		return profileName;
	}
	
	protected Map<String, Double> getValuesOf(String name){
		return indicators.get(name).getCoefficients();
	}
	
	protected abstract double parseCategorieFromIndicator(Object objectToScore, P properties, HumanIndicator indicator);
	
	protected abstract Object parseTextFromIndicator(Object objectToScore, P properties, String text) throws NullPointerException;
	
}
