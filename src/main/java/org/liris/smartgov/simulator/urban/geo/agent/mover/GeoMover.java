package org.liris.smartgov.simulator.urban.geo.agent.mover;

import org.liris.smartgov.simulator.urban.geo.utils.LatLon;

/**
 * Mover interface for geographical environments.
 */
public interface GeoMover {

	public LatLon moveOn(double distance);
	
}
