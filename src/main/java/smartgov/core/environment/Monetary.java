package smartgov.core.environment;

/**
 * Implementing this interface allows an object to be a paying object.
 * It displays the current price and can update the current price.
 * @author spageaud
 *
 */
public interface Monetary {
	
	double getPrice();
	
	void updatePrice(double priceToAdd);
	
}
