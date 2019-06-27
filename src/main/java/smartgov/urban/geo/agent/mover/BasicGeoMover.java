package smartgov.urban.geo.agent.mover;

import java.util.ArrayList;
import java.util.Collection;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.agent.moving.Plan;
import smartgov.core.events.EventHandler;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.event.GeoMoveEvent;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.simulation.GISComputation;

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
	 * Move agent on the current arc.
	 *
	 * @param timeToMove unknown.
	 */
	// TODO: refactor parameter...
	@Override
	public Coordinate moveOn(double distance){
		// TODO : make destination a parameter, and perform updates using listeners.
		updateAgentSpeed(agentBody);
		// double distance = agentBody.getSpeed() * SmartGov.getRuntime().getTickDuration();
		Plan plan = agentBody.getPlan();
		Coordinate currentPosition = agentBody.getPosition();
		if(!plan.isPathComplete()){
			GeoArc arc = (GeoArc) plan.getCurrentArc();
			Coordinate destination = ((GeoNode) plan.getNextNode()).getPosition();
			
			// updateAgent(arc, agentBody);
			double remainingDistanceToNode = GISComputation.GPS2Meter(currentPosition, destination);
			if(remainingDistanceToNode < distance){
				// Reach a node on the distance to cross
				currentPosition = destination;
				plan.reachNextNode();
				
				GeoArc newArc = (GeoArc) plan.getCurrentArc();
				
				if(newArc != null){
					// If this is not the last node, cross the remaining distance on the next arc
					handleArcChanged(arc, newArc);
					
					// New remaining time, assuming that 
					return moveOn(distance - remainingDistanceToNode);
				}
			} else {
				/*
				 * We are working in latitude / longitude there.
				 * 
				 * What we know :
				 *  - currentCoordinates (x1, y1) and nextNodeCoordinates (x2, y2) in latitude / longitude
				 *  - distance in meter between the current position and the next node : d_to_node
				 *  - the distance to cross in meter during this iteration ("distance") : d_to_travel
				 * 
				 * What we want :
				 * 	- dx, dy, in longitude / latitude, such as (x1 + dx, y1 + dy) are the new coordinates at the
				 *    end of the iteration.
				 * 
				 * Solution :
				 *  let x[°] be a polar coordinate and x[m] the corresponding plan coordinate in meter.
				 *  
				 *  Because the distance there is very small, we assume the linear relation :
				 *   x_2[m] - x_1[m] = k * ( x_2[°] - x_1[°] ) for any point x_2, x_1, where k is an unknown constant.
				 *   
				 *  From there : 
				 *   dx[m] / (x_node[m] - x_origin[m]) = dx[°] / (x_node[°] - x_origin[°])
				 *   
				 *  But also, from the Thales theorem :
				 *   dx[m] / (x_node[m] - x_origin[m]) = d_to_travel[m] / d_to_node[m]
				 *   
				 *  Finally :
				 *   dx[°] = (x_node[°] - x_origin[°]) * d_to_travel[m] / d_to_node[m]
				 * 
				 */
				double dx = distance * (destination.x - currentPosition.x) / remainingDistanceToNode;
				double dy = distance * (destination.y - currentPosition.y) / remainingDistanceToNode;
				Coordinate newCoordinate = new Coordinate(currentPosition.x + dx, currentPosition.y + dy);
				triggerCarMovedListeners(
						new GeoMoveEvent(
								currentPosition,
								newCoordinate,
								GISComputation.GPS2Meter(currentPosition, newCoordinate)
								)
						);
				return newCoordinate;
			}
		}
		
		return currentPosition;
	}
	
	/**
	 * Defines some behavior that should be applied each time an agent reaches the next arc
	 * of the current plan from the mover.
	 * 
	 * <p>
	 * Example : notify the road containing the new arc that a new agent arrives to handle
	 * agents interactions. 
	 * </p>
	 * 
	 * @param oldArc Previous arc
	 * @param newArc Current arc
	 */
	protected void handleArcChanged(GeoArc oldArc, GeoArc newArc) {
		
	};
	
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
	
	private void triggerCarMovedListeners(GeoMoveEvent event) {
		for (EventHandler<GeoMoveEvent> listener : geoMoveListeners) {
			listener.handle(event);
		}
	};
}
