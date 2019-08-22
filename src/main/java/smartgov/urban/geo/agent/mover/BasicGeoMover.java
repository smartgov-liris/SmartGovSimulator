package smartgov.urban.geo.agent.mover;

import java.util.ArrayList;
import java.util.Collection;

import smartgov.core.agent.moving.plan.Plan;
import smartgov.core.events.EventHandler;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.event.GeoMoveEvent;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.utils.LatLon;

public class BasicGeoMover implements GeoMover {
	
	protected GeoAgentBody agentBody;

	private Collection<EventHandler<GeoMoveEvent>> geoMoveListeners;
	
	/**
	 * BasicGeoMover constructor.
	 */
	public BasicGeoMover() {
		this.geoMoveListeners = new ArrayList<>();
	}
	
	/**
	 * Sets the body associated to this mover.
	 *
	 * @param agentBody agent body
	 */
	public void setAgentBody(GeoAgentBody agentBody) {
		this.agentBody = agentBody;
	}
	
	/**
	 * Move agent on the current arc. Reaches nodes of the plan as required
	 * while crossing the specified distance.
	 *
	 * @param distance distance to move
	 */
	@Override
	public LatLon moveOn(double distance){
		updateAgentSpeed(agentBody);
		Plan plan = agentBody.getPlan();
		LatLon currentPosition = agentBody.getPosition();
		if(!plan.isComplete()){
			GeoNode destination = ((GeoNode) plan.getNextNode());
			
			// updateAgent(arc, agentBody);
			double remainingDistanceToNode = LatLon.distance(currentPosition, destination.getPosition());
			if(remainingDistanceToNode < distance){
				// Reach a node on the distance to cross
				currentPosition = destination.getPosition();
				plan.reachNextNode();
				
				if(!plan.isComplete()) {
					// If this is not the last node, cross the remaining distance on the next arc
					return moveOn(distance - remainingDistanceToNode);
				}
				
			} else {
				/*
				 * We are working in latitude / longitude there.
				 * 
				 * What we know :
				 *  - current agent coordinates (x_current, y_current)
				 *  - next node coordinates (x_node, y_node) in latitude / longitude
				 *  - distance in meter between the current position and the next node : d_to_node
				 *  - the distance to cross in meter during this iteration ("distance") : d_to_travel
				 * 
				 * What we want :
				 * 	- dx, dy, in latitude / longitude, such as (x_next, y_next) = (x_current + dx, y_current + dy) are the new coordinates at the
				 *    end of the iteration.
				 * 
				 * Solution :
				 *  let x[°] be a polar coordinate and x[m] the corresponding plan coordinate in meter.
				 *  
				 *  Because the distance there is very small, we assume the linear relation :
				 *   x_2[m] - x_1[m] = k * ( x_2[°] - x_1[°] ) for any point x_2, x_1, where k is an unknown constant.
				 *   
				 *  From there : 
				 *   dx[m] = x_next[m] - x_current[m]
				 *   dx[m] = k * (x_next[°] - x_current[°]) = k * dx[°]
				 *   x_node[m] - x_current[m] = k * (x_next[°] - x_current[°])
				 *   
				 *   dx[m] / (x_node[m] - x_current[m]) = dx[°] / (x_node[°] - x_current[°])
				 *   
				 *  But also, from the Thales theorem :
				 *   dx[m] / (x_node[m] - x_current[m]) = d_to_travel[m] / d_to_node[m]
				 *   
				 *  Finally :
				 *   dx[°] = (x_node[°] - x_current[°]) * d_to_travel[m] / d_to_node[m]
				 * 
				 */
				double dx = distance * (destination.getPosition().lat - currentPosition.lat) / remainingDistanceToNode;
				double dy = distance * (destination.getPosition().lon - currentPosition.lon) / remainingDistanceToNode;
				LatLon newCoordinate = new LatLon(currentPosition.lat + dx, currentPosition.lon + dy);
				triggerGeoMoveListeners(
						new GeoMoveEvent(
								currentPosition,
								newCoordinate,
								LatLon.distance(currentPosition, newCoordinate)
								)
						);
				return newCoordinate;
			}
		}
		
		return currentPosition;
	}
	
	/**
	 * Called immediately when the {@link #moveOn moveOn()} function is called.
	 * 
	 * By default, will keep the agent speed constant.
	 * 
	 * Can be use to manage acceleration depending on environment perceptions.
	 * 
	 * @param agentBody Moving agent body
	 */
	protected void updateAgentSpeed(GeoAgentBody agentBody) {
		
	};
	
	/**
	 * Adds a new GeoMoveEvent handler.
	 *
	 * <p>
	 * Triggered each time the car moves : can be triggered several times
	 * in the same tick, depending on the tick duration and the agent
	 * speed.
	 * </p>
	 * 
	 * <p>
	 * Such events can be used to handle crossed distance because sent events
	 * containes geographical information, whereas classic move event do not.
	 * </p>
	 * 
	 * @param listener new car moved event handler to add
	 */
	public void addGeoMoveEventListener(EventHandler<GeoMoveEvent> listener) {
		geoMoveListeners.add(listener);
	}
	
	private void triggerGeoMoveListeners(GeoMoveEvent event) {
		for (EventHandler<GeoMoveEvent> listener : geoMoveListeners) {
			listener.handle(event);
		}
	};
}
