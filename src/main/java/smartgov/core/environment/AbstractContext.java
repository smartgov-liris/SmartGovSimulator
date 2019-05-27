package smartgov.core.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import smartgov.SmartGov;
import smartgov.core.simulation.Files;
import smartgov.core.simulation.Scenario;

/**
 * Abstract environment class, implemented at different SmartGov levels.
 * 
 * @author pbreugnot
 *
 */
public abstract class AbstractContext {
	
	private Properties config;
	private Files files;

	public abstract void clear();
	public abstract void init();
	
	public AbstractContext(String configFile) {
		parseConfig(configFile);
	}
	
	public abstract Scenario loadScenario(String scenarioName);
	
	private void parseConfig(String file) {
		SmartGov.logger.info("Loading config from " + file);
		config = new Properties();
		try {
			File configFile = new File(file);
			files = new Files(configFile.getParentFile().getAbsolutePath(), config);
			config.load(new FileInputStream(new File(file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Properties getConfig() {
		return config;
	}
	
	public Files getFiles() {
		return files;
	}
	
//	public static Map<String, String> parseConfig(String filestr) {
//		Map<String, String> config = new HashMap<>();
//		File file = new File(filestr);
//		Scanner input;
//		try {
//			input = new Scanner(file);
//			
//			while(input.hasNext()) {
//			    String nextLine = input.nextLine();
//			    if(!nextLine.contains("#")){
//			    	if(nextLine.contains(",")){
//			    		//Indicators
//			    		String lines[] = nextLine.split(":");
//			    		String indicators[] = lines[1].split(",");
//			    		for(int indicatorIndex = 0; indicatorIndex < indicators.length; indicatorIndex ++){
//			    			indicatorsFiles.put(indicators[indicatorIndex], FilePath.humanIndicatorFolder + indicators[indicatorIndex] + ".json");
//			    		}
//			    	} else {
//			    		String lines[] = nextLine.split(":");
//				    	config.put(lines[0], lines[1]);
//			    	}
//			    }
//			}
//			input.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		return config;
//	}
}
