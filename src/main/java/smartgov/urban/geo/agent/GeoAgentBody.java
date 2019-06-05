package smartgov.urban.geo.agent;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.math.Vector2D;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import smartgov.core.agent.moving.MovingAgentBody;
import smartgov.core.agent.moving.ParkingArea;
import smartgov.core.agent.moving.Plan;
import smartgov.core.main.SimulationBuilder;
import smartgov.core.output.coordinate.CoordinateSerializer;
/**
 * A generic abstract child of an AbstractAgentBody, built to be represented on a map.
 * 
 * @author pbreugnot
 *
 * @param <Node> GeoNode type
 * @param <Arc> GeoArc type
 * @param <Tmover> Mover used to move agents in the structure.
 * @param <Tsensor> Sensor used to sense data in the environment.
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

	public GeoAgentBody(GeoMover mover) {
		this.mover = mover;
		speed = 0.0;
		direction = new Vector2D();
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public void setSpeed(double speed) {
		if(Double.isNaN(speed)){
			this.speed = 0.0;
		} else {
			this.speed = speed;
		}
	}

	public double getSpeed() {
		return speed;
	}

	public void setDestination(Coordinate destination) {
		this.destination = destination;
	}

	public Vector2D getDirection() {
		return direction;
	}

	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}
	
	public GeoMover getMover() {
		return mover;
	}

	@Override
	public void handleMove() {
		// Distance to cross in one tick
		double distance = getSpeed() * SimulationBuilder.TICK_DURATION;
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
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
}
