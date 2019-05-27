package smartgov.core.utils;

import java.io.File;
import java.util.Properties;

/**
 * Store important filename during simulation
 * @author spageaud
 *
 * TODO: Split by level
 */
public class Files {
	
	private String root;
	private Properties config;
	
	public Files(String root, Properties config) {
		this.root = root;
		this.config = config;
	}
	
	public String getFile(String file) {
		String relativeFilePath = config.getProperty(file);
		if (relativeFilePath == null) {
			throw new IllegalArgumentException("No entry found in config file for " + file);
		}
		return root + File.separator + relativeFilePath;
	}
}
