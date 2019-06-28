package smartgov.core.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import smartgov.SmartGov;
import smartgov.core.simulation.Scenario;
import smartgov.core.utils.FileLoader;

/**
 * Abstract simulation context.
 *
 * <p>
 * Defines functions to load configuration and scenarios.
 * </p>
 *
 * @author pbreugnot
 *
 */
public abstract class AbstractContext {

	private Scenario scenario;
	
	private Properties config;
	private FileLoader fileLoader;

	/**
	 * Clears all the collections of simulation items
	 * stored in the context.
	 */
	public abstract void clear();
	
	/**
	 * Instantiates a context from the specified configFile and loads the
	 * specified scenario (but don't build it yet).
	 *
	 * @param configFile Absolute path of the configuration file to load.
	 */
	public AbstractContext(String configFile) {
		parseConfig(configFile);
		scenario = loadScenario((String) config.get("scenario"));
	}
	
	/**
	 * Current scenario.
	 *
	 * @return currently loaded scenario
	 */
	public Scenario getScenario() {
		return scenario;
	}
	
	/**
	 * Loads a scenario from the given name.
	 *
	 * @param scenarioName Name of the scenario to load.
	 * @return new scenario instance
	 */
	public abstract Scenario loadScenario(String scenarioName);
	
	private void parseConfig(String file) {
		SmartGov.logger.info("Loading config from " + file);
		config = new Properties();
		try {
			File configFile = new File(file);
			fileLoader = new FileLoader(configFile.getParentFile().getAbsolutePath(), config);
			config.load(new FileInputStream(new File(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Current simulation configuration, loaded from the configuration
	 * file.
	 *
	 * @return current configuration
	 */
	public Properties getConfig() {
		return config;
	}
	
	/**
	 * File loader.
	 *
	 * @see smartgov.core.utils.FileLoader
	 * @return a file loaded associated to the current configuration
	 */
	public FileLoader getFileLoader() {
		return fileLoader;
	}
	
}
