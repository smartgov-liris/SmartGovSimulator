package smartgov.models.lez.copert;

import java.util.HashMap;

import smartgov.models.lez.copert.fields.Pollutant;
import smartgov.models.lez.copert.tableParser.CopertParser;
import smartgov.models.lez.copert.tableParser.CopertTree;

/**
 * Utility class to store {@link smartgov.models.lez.copert.CopertParameters CopertParameters} for
 * each pollutant types.
 * 
 * @author pbreugnot
 *
 */
public class Copert {

	private HashMap<Pollutant, CopertParameters> copertParameters;
	
	/**
	 * Build a copertParameters for each {@link smartgov.models.lez.copert.fields.Pollutant Pollutant} 
	 * according to the given CopertTree.
	 * If at some point in the corresponding query no sub-classes is found, the copertParameters will be
	 * set to null.
	 * 
	 * @param copertTree an initialized copertTree
	 */
	public Copert(CopertTree completeTree) {
		copertParameters = new HashMap<>();
		
		for(Pollutant pollutant : Pollutant.values()) {
			copertParameters.put(
					pollutant,
					CopertParser.copertParameters(
							completeTree,
							pollutant
							)
					);
		}
	}
	
	/**
	 * Returns the copertParameters according for the given
	 * 
	 * {@link smartgov.models.lez.copert.fields.Pollutant Pollutant}.
	 * @param pollutant Pollutant
	 * @return Copert parameters
	 */
	public CopertParameters getCopertParameters(Pollutant pollutant) {
		return copertParameters.get(pollutant);
	}
}
