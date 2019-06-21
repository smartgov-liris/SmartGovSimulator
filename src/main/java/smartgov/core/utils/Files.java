package smartgov.core.utils;

import java.io.File;
import java.util.Properties;

/**
 * Utility class to load files and folders specified in the configuration file.
 *
 * Paths are loaded relatively to the directory of the configuration file
 * itself.
 */
public class Files {
	
	private String root;
	private Properties config;
	
	/**
	 * Create a Files instance from a root directory and a
	 * loaded configuration.
	 * 
	 * @param root root directory
	 * @param config loaded configuration file
	 */
	public Files(String root, Properties config) {
		this.root = root;
		this.config = config;
	}
	
	/**
	 * Compute the path of the specified file, as an entry of the
	 * configuration file.
	 *
	 * @param file configuration file entry that correspond to a file or
	 * directory name
	 * @return file or directory path
	 */
	public String getFile(String file) {
		String relativeFilePath = config.getProperty(file);
		if (relativeFilePath == null) {
			throw new IllegalArgumentException("No entry found in config file for " + file);
		}
		return root + File.separator + relativeFilePath;
	}
}
