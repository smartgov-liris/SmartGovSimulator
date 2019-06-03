package smartgov.urban.osm.agent.actuator;

import org.locationtech.jts.geom.Coordinate;

import smartgov.urban.geo.environment.graph.GeoNode;
import smartgov.urban.geo.simulation.GISComputation;

/**
 * Implementation of Gipps steering behavior explained in the following paper :
 * @see <a href="http://turing.iimas.unam.mx/sos/sites/default/files/Gipps_ABehaviouralCarFollowingModel.pdf">Gipps' Steering Behavior</a>
 */
public class GippsSteering {
	
	private double a_n; //maximum acceleration which the driver of vehicle n wishes to undertake
	private double b_n; //most severe braking that the driver of vehicle n wishes to undertake (b_n < 0)
	//private double s_n; //effective size of vehicle n, that is, the physical length plus a margin into which the following vehicle is not willing to intrude, even when at rest
	//private double V_n; //speed at which the driver of vehicle n wishes to travel
	//private double x_n_t; //location of the front of vehicle n at time t
	//private double v_n_t; //speed of vehicle n at time t
	private double teta; //apparent reaction time, a constant for all vehicles
	
	public GippsSteering(double teta, double a_n, double b_n){
		this.a_n = a_n;
		this.teta = teta;
		this.b_n = b_n;
	}
	
	
	/**
	 * Compute speed with steering behavior by using minimum between acceleration and braking
	 * @param v_n_t speed of vehicle n at time t
	 * @param V_n speed at which the driver of vehicle n wishes to travel
	 * @param x_n_1_t location of the front vehicle n-1 at time t
	 * @param v_n_1_t speed of vehicle n-1 at time t
	 * @param s_n_1 effective size of vehicle n-1
	 * @param b_n_1 assumption of most severe braking of vehicle n-1
	 * @param x_n_t location of the front of vehicle n at time t
	 * @return new speed for vehicle n
	 */
	public double getSpeed(double v_n_t, double V_n, Coordinate x_n_1_t, double v_n_1_t, double s_n_1, double b_n_1, Coordinate x_n_t){
		//System.out.println("Acceleration: " +getAcceleration(v_n_t, V_n) + ", braking: " + getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
		double speed = Math.min(getAcceleration(v_n_t, V_n), getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
		//System.out.print(", steering speed computation: " + speed + ", braking: " + getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t) + ", acceleration: " + getAcceleration(v_n_t, V_n));
		return speed < 0 ? 0 : speed;
		//return Math.min(getAcceleration(v_n_t, V_n), getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
	}
	
	/**
	 * Compute speed without considering any leader.
	 * @param v_n_t speed of vehicle n at time t
	 * @param V_n speed at which the driver of vehicle n wishes to travel
	 * @return new speed for vehicle n
	 */
	public double getSpeedWithoutLeader(double v_n_t, double V_n) {
		return getAcceleration(v_n_t, V_n);
	}
	//*/
//	public double getSpeed(double v_n_t, double V_n, GeoNode targetType, double v_n_1_t, double s_n_1, double b_n_1, Coordinate x_n_t){
//		Coordinate positionOfTarget = targetType.getPosition();
//		// TODO: clear thar function
//		// targetType used to be a WorldObject, but this is no more the case.
//		if(targetType instanceof GeoNode){ // ???????
//			// targetType = Node => no leader
//			double speed = getAcceleration(v_n_t, V_n);
//			return speed < 0 ? 0 : speed;
//		} else{
//			//System.out.println("Acceleration: " +getAcceleration(v_n_t, V_n) + ", braking: " + getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
//			double speed = Math.min(getAcceleration(v_n_t, V_n), getBraking(positionOfTarget, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
//			return speed < 0 ? 0 : speed;
//			//return Math.min(getAcceleration(v_n_t, V_n), getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
//		}
//	}
	
	//*/
	
	/**
	 * Compute acceleration for vehicle n
	 * @param v_n_t speed of vehicle n at time t
	 * @param V_n speed at which the driver of vehicle n wishes to travel
	 * @return double acceleration
	 */
	private double getAcceleration(double v_n_t, double V_n){
		return (v_n_t + 2.5*a_n*teta*(1 - v_n_t/V_n)*Math.sqrt(0.025+v_n_t/V_n));
	}
	
	/**
	 * Braking speed
	 * @param x_n_1_t location of the front vehicle n-1 at time t
	 * @param v_n_1_t speed of vehicle n-1 at time t
	 * @param s_n_1 effective size of vehicle n-1
	 * @param v_n_t speed of vehicle n at time t
	 * @param b_n_1 assumption of most severe braking of vehicle n-1
	 * @param x_n_t location of the front of vehicle n at time t
	 * @return braking speed to avoid collision in a smooth manner
	 */
	private double getBraking(Coordinate x_n_1_t, double v_n_1_t, double s_n_1, double v_n_t, double b_n_1, Coordinate x_n_t){
		//System.out.println(", - " + b_n + "*(2*" + distanceBetweenTwoCars(x_n_t, s_n_1, x_n_1_t) + " - " + v_n_t + "*"+ teta + " - Math.pow(" + v_n_1_t + ",2)/" + b_n_1 + "): " + - b_n*(2*distanceBetweenTwoCars(x_n_t, s_n_1, x_n_1_t) - v_n_t*teta - Math.pow(v_n_1_t,2)/b_n_1));
		//System.out.println(- b_n*(2*Math.floor(distanceBetweenTwoCars(x_n_t, s_n_1, x_n_1_t)) - Math.floor(v_n_t)*teta - Math.pow(Math.floor(v_n_1_t),2)/b_n_1));
		return b_n*teta + Math.sqrt(Math.pow(b_n, 2)*Math.pow(teta, 2) - b_n*(2*distanceBetweenTwoCars(x_n_t, s_n_1, x_n_1_t) - v_n_t*teta - Math.pow(v_n_1_t,2)/b_n_1));
	}
	
	/**
	 * Compute distance between two cars using both front coordinates and the size of the leader car
	 * @param x_n_t location of the vehicle n at time t
	 * @param s_n_1 effective size of vehicle n-1
	 * @param x_n_1_t location of the vehicle n-1 at time t
	 * @return distance in meters
	 */
	private double distanceBetweenTwoCars(Coordinate x_n_t, double s_n_1, Coordinate x_n_1_t){
		//Compare GPS coordinates with meters !
		//return GISComputation.GPS2Meter(new Coordinate(x_n_1_t.x - s_n_1, x_n_1_t.y - s_n_1), x_n_t );
		return GISComputation.GPS2Meter(x_n_1_t, x_n_t) - s_n_1;
	}

}
