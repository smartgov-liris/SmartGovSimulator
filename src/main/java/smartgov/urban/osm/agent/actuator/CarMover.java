package smartgov.urban.osm.agent.actuator;

import java.util.ArrayList;
import java.util.Collection;

import org.locationtech.jts.geom.Coordinate;

import smartgov.SmartGov;
import smartgov.core.agent.moving.Plan;
import smartgov.core.events.EventHandler;
import smartgov.urban.geo.agent.GeoMover;
import smartgov.urban.geo.agent.event.CarMovedEvent;
import smartgov.urban.geo.simulation.GISComputation;
import smartgov.urban.osm.agent.OsmAgentBody;
import smartgov.urban.osm.environment.graph.OsmArc;
import smartgov.urban.osm.environment.graph.OsmNode;

/**
 * Used to move an OsmAgentBody in the osm graph.
 * 
 * @author pbreugnot
 *
 */
public class CarMover implements GeoMover {
	
	protected OsmAgentBody agentBody;
	private GippsSteering gippsSteering;
	
	private Collection<EventHandler<CarMovedEvent>> carMovedListeners;

	public CarMover() {
		carMovedListeners = new ArrayList<>();
		gippsSteering = new GippsSteering(1.0,1.5,-3.0);
	}
	
	public void setAgentBody(OsmAgentBody agentBody) {
		this.agentBody = agentBody;
	}
	
	public GippsSteering getSteering() {
		return gippsSteering;
	}
	
	/**
	 * Move agent on the current arc.
	 */
	@Override
	public Coordinate moveOn(double timeToMove){
		// TODO : make destination a parameter, and perform updates using listeners.
		updateAgentSpeed(agentBody);
		double distance = agentBody.getSpeed() * SmartGov.getRuntime().getTickDuration();
		Plan plan = agentBody.getPlan();
		Coordinate currentPosition = agentBody.getPosition();
		if(!plan.isPathComplete()){
			OsmArc arc = (OsmArc) plan.getCurrentArc();
			Coordinate destination = ((OsmNode) plan.getNextNode()).getPosition();
			
			// updateAgent(arc, agentBody);
			double remainingDistanceToNode = GISComputation.GPS2Meter(currentPosition, destination);
			if(remainingDistanceToNode < distance){
				// Reach a node on the distance to cross
				arc.getRoad().getAgentsOnPath().remove(agentBody);
				currentPosition = destination;
				plan.reachNextNode();
				
				arc = (OsmArc) plan.getCurrentArc();
				
				if(arc != null){
					// If this is not the last node, cross the remaining distance on the next arc
					updateAgent(arc, agentBody);
					
					// New remaining time, assuming that 
					return moveOn((distance - remainingDistanceToNode) / agentBody.getSpeed());
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
						new CarMovedEvent(
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
	
	private void updateAgent(OsmArc arc, OsmAgentBody agentBody){
		if (!arc.getRoad().getAgentsOnPath().contains(agentBody)) {
			arc.getRoad().getAgentsOnPath().add(agentBody);
		}
		agentBody.setDirection(arc.getDirection());
	}
	
	private void updateAgentSpeed(OsmAgentBody agentBody) {
		OsmAgentBody leader = ((OsmArc) agentBody.getPlan().getCurrentArc()).getRoad().getLeaderOfAgent(agentBody);
		if (leader != null) {
			agentBody.setSpeed(
					gippsSteering.getSpeed(
						agentBody.getSpeed(), // current speed
						40, // Max speed
						leader.getPosition(), // Position of the leader
						leader.getSpeed(),
						7.5,
						-3,
						agentBody.getPosition())
					);
		}
		else {
			agentBody.setSpeed(
					gippsSteering.getSpeedWithoutLeader(
						agentBody.getSpeed(),
						40)
					);
		}
	}
	
	public void addCarMovedEventListener(EventHandler<CarMovedEvent> listener) {
		carMovedListeners.add(listener);
	}
	
	private void triggerCarMovedListeners(CarMovedEvent event) {
		for (EventHandler<CarMovedEvent> listener : carMovedListeners) {
			listener.handle(event);
		}
	}
}
