package smartgov.urban.geo.agent.mover;

import java.util.ArrayList;
import java.util.Collection;

import org.locationtech.jts.geom.Coordinate;

import smartgov.core.agent.moving.plan.Plan;
import smartgov.core.events.EventHandler;
import smartgov.urban.geo.agent.GeoAgentBody;
import smartgov.urban.geo.agent.event.GeoMoveEvent;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.utils.GISComputation;

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
	public Coordinate moveOn(double distance){
		updateAgentSpeed(agentBody);
		Plan plan = agentBody.getPlan();
		Coordinate currentPosition = agentBody.getPosition();
		if(!plan.isPlanComplete()){
			GeoArc arc = (GeoArc) plan.getCurrentArc();
			GeoNode destination = ((GeoNode) plan.getNextNode());
			
			// updateAgent(arc, agentBody);
			double remainingDistanceToNode = GISComputation.GPS2Meter(currentPosition, destination.getPosition());
			if(remainingDistanceToNode < distance){
				// Reach a node on the distance to cross
				currentPosition = destination.getPosition();
				plan.reachNextNode();
				
				handleArcChanged(arc, (GeoArc) plan.getCurrentArc());
				
				if(!plan.isPlanComplete()) {
					// If this is not the last node, cross the remaining distance on the next arc
					return moveOn(distance - remainingDistanceToNode);
				}
				else {
					handleDestinationReached(agentBody, arc, destination);
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
				double dx = distance * (destination.getPosition().x - currentPosition.x) / remainingDistanceToNode;
				double dy = distance * (destination.getPosition().y - currentPosition.y) / remainingDistanceToNode;
				Coordinate newCoordinate = new Coordinate(currentPosition.x + dx, currentPosition.y + dy);
				triggerGeoMoveListeners(
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
	 * Called immediately after the agent as reach it's destination from the 
	 * <code>moveOn()</code> function, before any other initialization occured.
	 * 
	 * <p>
	 * Useful for it's reference to the last arc and node crossed.
	 * </p>
	 * <p>
	 * Example : To remove an agent from the last road once it has reached the 
	 * end of it.
	 * </p>
	 * 
	 * <p>
	 * By default, does nothing.
	 * </p>
	 * 
	 * @param agentBody Agents that reached its destination
	 * @param lastArc last arc crossed
	 * @param lastNode last node crossed
	 */
	protected void handleDestinationReached(GeoAgentBody agentBody, GeoArc lastArc, GeoNode lastNode) {
		
	}
	
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
