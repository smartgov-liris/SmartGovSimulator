package smartgov.urban.geo.agent;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.math.Vector2D;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.SmartGov;
import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.ParkingArea;
import smartgov.core.output.coordinate.CoordinateSerializer;
import smartgov.urban.geo.agent.mover.GeoMover;

/**
 * Body of a GeoAgent.
 *
 * Represents the part of the agent that will actually move in the
 * geographical environment.
 *
 * @see GeoAgent
 */
public class GeoAgentBody extends MovingAgentBody {

	@JsonIgnore
	protected Vector2D direction;

	protected double speed; //In meters per second
	
	@JsonIgnore
	protected Coordinate destination;
	@JsonSerialize(using=CoordinateSerializer.class)
	protected Coordinate position;
	@JsonIgnore
	protected GeoMover mover;

	/**
	 * GeoAgentBody constructor.
	 *
	 * @param mover mover that will be used to handle move actions
	 */
	public GeoAgentBody(GeoMover mover) {
		this.mover = mover;
		speed = 0.0;
		direction = new Vector2D();
	}

	/**
	 * Current position of the agent, in longitude / latitude.
	 *
	 * @return position of the agent
	 */
	public Coordinate getPosition() {
		return position;
	}

	/**
	 * Sets the position of the agent, in longitude / latitude.
	 *
	 * @param position new body position
	 */
	public void setPosition(Coordinate position) {
		this.position = position;
	}

	/**
	 * Sets this agent body speed.
	 *
	 * @param speed new agent body speed
	 */
	public void setSpeed(double speed) {
		if(Double.isNaN(speed)){
			this.speed = 0.0;
		} else {
			this.speed = speed;
		}
	}

	/**
	 * Current speed of this agent body.
	 *
	 * @return speed of this agent body
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Utility parameter that represents the orientation of the agent on
	 * the map.
	 *
	 * Might be used for graphical representations.
	 *
	 * @deprecated
	 * @return agent direction
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of this agent body on the map.
	 *
	 * @param direction new agent body direction
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	/**
	 * Current mover of this agent body.
	 *
	 * @return Mover of this agent body.
	 */	
	public GeoMover getMover() {
		return mover;
	}

	/**
	 * Predefined move action handler.
	 *
	 * Will be called on MOVE actions, as specified in the
	 * {@link smartgov.core.agent.moving.MovingAgentBody MovingAgentBody}
	 * class.
	 *
	 * <p>
	 * Computed the distance to cross of this tick from the runtime tick
	 * duration, and cross the distance thanks to the GeoMover
	 * {@link GeoMover#moveOn moveOn()} function.
	 */
	@Override
	public void handleMove() {
		// Distance to cross in one tick
		double distance = getSpeed() * SmartGov.getRuntime().getTickDuration();
		setPosition(this.mover.moveOn(distance));
	}

	@Override
	public void handleEnter(ParkingArea parkingArea) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLeave(ParkingArea parkingArea) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleWait() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleWander() {
		// TODO Auto-generated method stub
		
	}
	
}
