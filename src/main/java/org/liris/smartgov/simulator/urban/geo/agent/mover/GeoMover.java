package org.liris.smartgov.simulator.urban.geo.agent.mover;

import org.liris.smartgov.simulator.urban.geo.agent.GeoAgentBody;
import org.liris.smartgov.simulator.urban.geo.utils.LatLon;

/**
 * Mover abstract class for geographical environments.
 */
public abstract class GeoMover {

	protected GeoAgentBody agentBody;
	
	/**
	 * Makes the agent body move on the following distance in meter,
	 * and returns its new geographical coordinates.
	 * 
	 * <p>
	 * Used by the {@link org.liris.smartgov.simulator.urban.geo.agent.GeoAgentBody}
	 * to handle MOVE events.
	 * </p>
	 * 
	 * @param distance to move on, in meter
	 * @return new geographical position
	 */
	public abstract LatLon moveOn(double distance);

	/**
	 * Sets the body associated to this mover.
	 *
	 * @param agentBody agent body
	 */
	public void setAgentBody(GeoAgentBody agentBody) {
		this.agentBody = agentBody;
	}
}
