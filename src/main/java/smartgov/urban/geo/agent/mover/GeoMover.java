package smartgov.urban.geo.agent.mover;

import smartgov.urban.geo.utils.LatLon;

/**
 * Mover interface for geographical environments.
 */
public interface GeoMover {

	public LatLon moveOn(double distance);
	
}
