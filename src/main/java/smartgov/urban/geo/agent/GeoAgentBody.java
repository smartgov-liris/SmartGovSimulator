package smartgov.urban.geo.agent;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.math.Vector2D;

import com.fasterxml.jackson.annotation.JsonIgnore;

import smartgov.core.agent.AbstractAgentBody;
import smartgov.core.agent.Plan;
import smartgov.core.agent.mover.AbstractMover;
import smartgov.core.main.SimulationBuilder;
import smartgov.urban.geo.agent.event.GeoMoveEvent;
import smartgov.urban.geo.environment.graph.GeoArc;
import smartgov.urban.geo.environment.graph.GeoNode;
/**
 * A generic abstract child of an AbstractAgentBody, built to be represented on a map.
 * 
 * @author pbreugnot
 *
 * @param <Tnode> GeoNode type
 * @param <Tarc> GeoArc type
 * @param <Tmover> Mover used to move agents in the structure.
 * @param <Tsensor> Sensor used to sense data in the environment.
 */
public abstract class GeoAgentBody<Tnode extends GeoNode<Tarc>, Tarc extends GeoArc<Tnode>, Tagent extends GeoAgent<?, ?, ?, ?>, Tmover extends AbstractMover> extends AbstractAgentBody<Tagent, Tnode, Tarc> {

	@JsonIgnore
	protected Vector2D direction;

	protected double speed; //In meters per second
	
	@JsonIgnore
	protected Coordinate destination;
	@JsonIgnore
	protected Coordinate position;

	@JsonIgnore
	protected Tmover mover;

	public GeoAgentBody() {
		super();
		speed = 0.0;
		direction = new Vector2D();
		plan = new Plan<Tnode, Tarc>();
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
	
	public Tmover getMover() {
		return mover;
	}

	@Override
	public GeoMoveEvent move() {
		Coordinate oldCoordinate = getPosition();
		Tarc oldArc = plan.getCurrentArc();
		Tnode oldNode = plan.getCurrentNode();
		
		// Distance to cross in one tick
		double distance = getSpeed() * SimulationBuilder.TICK_DURATION;

		Coordinate newCoordinate = getPosition();
		Tarc newArc = plan.getCurrentArc();
		Tnode newNode = plan.getCurrentNode();
		
		return new GeoMoveEvent(
				oldCoordinate,
				newCoordinate,
				oldArc,
				newArc,
				oldNode,
				newNode,
				distance
				);
	}

	@Override
	public void enter() {

	}

	@Override
	public void leave() {

	}

	@Override
	public void moveTo() {
		// TODO: to do.
	}

	@Override
	public void idle() {
		// TODO Auto-generated method stub

	}
}
