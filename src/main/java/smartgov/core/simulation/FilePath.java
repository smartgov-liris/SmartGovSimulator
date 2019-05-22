package smartgov.core.simulation;

import java.io.File;

/**
 * Stores relative path of important folders
 * @author spageaud
 *
 * TODO: Split by level
 *
 */
public abstract class FilePath {

	public static String backPath = ".." + File.separator;
	
	public static String externalSourceFolder     = "extsrc"                               + File.separator;
	public static String inputFolder              = "input"                                + File.separator;
	public static String humanAgentFolder         = inputFolder        + "humanAgents"     + File.separator;
	public static String structuresFolder         = inputFolder        + "structures"      + File.separator;
	public static String discretizerFolder        = inputFolder        + "discretizer"     + File.separator;
	public static String localLearnerFolder       = inputFolder        + "localLearner"    + File.separator;
	public static String GISFolder                = inputFolder        + "gis"             + File.separator;
	public static String synPopFolder             = inputFolder        + "synpop"          + File.separator;
	public static String behaviorFolder           = inputFolder        + "behavior"        + File.separator;
	public static String areaCoefFolder           = humanAgentFolder   + "areaCoef"        + File.separator;
	public static String humanIndicatorFolder     = humanAgentFolder   + "humanIndicators" + File.separator;
	public static String localLearnerModelsFolder = localLearnerFolder + "models"          + File.separator;
	public static String scenarioFolder           = humanAgentFolder   + "scenario"        + File.separator;
	public static String perimeterFolder          = inputFolder        + "perimeter"       + File.separator;
	
	public static String currentLocalLearnerFolder;
	public static String currentAgentDetailsFolder;
	public static String currentLocalLearnerCallbackFolder;
	
	public static String outputFolder             = "output" + File.separator;
	
}
