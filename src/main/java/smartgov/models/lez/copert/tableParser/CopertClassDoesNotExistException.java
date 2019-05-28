package smartgov.models.lez.copert.tableParser;

import java.util.NoSuchElementException;

/**
 * Exception thrown when a {@link smartgov.models.lez.copert.tableParser.CopertTree#select() select()}
 *  operation on a {@link smartgov.models.lez.copert.tableParser.CopertTree CopertTree}
 *   doesn't find anything.
 * 
 * @author pbreugnot
 *
 */
public class CopertClassDoesNotExistException extends NoSuchElementException{

	private static final long serialVersionUID = 1L;
	
	public CopertClassDoesNotExistException(String message) {
		super(message);
	}

}
