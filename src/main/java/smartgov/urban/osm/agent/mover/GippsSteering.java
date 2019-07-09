package smartgov.urban.osm.agent.mover;

/**
 * Implementation of Gipps steering behavior explained in the following paper :
 * @see <a href="http://turing.iimas.unam.mx/sos/sites/default/files/Gipps_ABehaviouralCarFollowingModel.pdf">Gipps' Steering Behavior</a>
 */
public class GippsSteering {
	
	/*
	 * Apparent reaction time in seconds, constant for all vehicles. Can be modified.
	 * By default, equal to 1.0
	 */
	public static double teta = 1.;
	
	private double maxAcceleration; //maximum acceleration which the driver of vehicle n wishes to undertake
	private double maxBraking; //most severe braking that the driver of vehicle n wishes to undertake (b_n < 0)

	
	/**
	 * Build a Gipps model instance, from the specified vehicle constants.
	 * 
	 * @param maxAcceleration maximum vehicle acceleration (m/s-2)
	 * @param maxBraking maximum vehicle braking, negative (m/s-2)
	 * @throws IllegalArgumentException if the specified acceleration is negative,
	 * the specified braking is positive.
	 */
	public GippsSteering(double maxAcceleration, double maxBraking){
		if (maxAcceleration <= 0) {
			throw new IllegalArgumentException("Maximum acceleration should be positive.");
		}
		if (maxBraking >= 0) {
			throw new IllegalArgumentException("Maximum braking should be negative.");
		}
		this.maxAcceleration = maxAcceleration;
		this.maxBraking = maxBraking;
	}
	
	
	/**
	 * Compute speed with steering behavior by using minimum between acceleration and braking
	 * @param followerSpeed speed of vehicle n at time t
	 * @param whishedFollowerSpeed speed at which the driver of vehicle n wishes to travel
	 * @param leaderSpeed speed of vehicle n-1 at time t
	 * @param leaderSize effective size of vehicle n-1
	 * @param maxLeaderBraking assumption of most severe braking of vehicle n-1
	 * @param distanceBetweenAgents distance between agents along the road, from front to front
	 * @return new speed for vehicle n
	 */
	public double getSpeed(
			double followerSpeed,
			double whishedFollowerSpeed,
			double leaderSpeed,
			double leaderSize,
			double maxLeaderBraking,
			double distanceBetweenAgents){
		//System.out.println("Acceleration: " +getAcceleration(v_n_t, V_n) + ", braking: " + getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
		double speed = Math.min(
				getAcceleration(followerSpeed, whishedFollowerSpeed),
				getBraking(distanceBetweenAgents, leaderSpeed, leaderSize, followerSpeed, maxLeaderBraking));
		//System.out.print(", steering speed computation: " + speed + ", braking: " + getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t) + ", acceleration: " + getAcceleration(v_n_t, V_n));
		return speed < 0 ? 0 : speed;
		//return Math.min(getAcceleration(v_n_t, V_n), getBraking(x_n_1_t, v_n_1_t, s_n_1, v_n_t, b_n_1, x_n_t));
	}
	
	/**
	 * Compute speed without considering any leader.
	 * @param currentSpeed speed of vehicle n at time t
	 * @param wishedSpeed speed at which the driver of vehicle n wishes to travel
	 * @return new speed for vehicle n
	 */
	public double getSpeedWithoutLeader(double currentSpeed, double wishedSpeed) {
		return getAcceleration(currentSpeed, wishedSpeed);
	}
	
	/**
	 * Compute acceleration for vehicle n
	 * @param currentSpeed speed of vehicle n at time t
	 * @param wishedSpeed speed at which the driver of vehicle n wishes to travel
	 * @return double acceleration
	 */
	private double getAcceleration(double currentSpeed, double wishedSpeed){
		return (currentSpeed + 2.5 * maxAcceleration * teta * (1 - currentSpeed / wishedSpeed)
					* Math.sqrt(0.025 + currentSpeed / wishedSpeed));
	}
	
	/**
	 * Braking speed
	 * @param distanceBetweenAgents distance between agents along the road, from front to front
	 * @param leaderSpeed speed of vehicle n-1 at time t
	 * @param leaderSize effective size of vehicle n-1
	 * @param followerSpeed speed of vehicle n at time t
	 * @param maximumLeaderBraking assumption of most severe braking of vehicle n-1
	 * @return braking speed to avoid collision in a smooth manner
	 */
	private double getBraking(
			double distanceBetweenAgents,
			double leaderSpeed,
			double leaderSize,
			double followerSpeed,
			double maximumLeaderBraking){
		return maxBraking*teta +
				Math.sqrt(
						Math.pow(maxBraking, 2)*Math.pow(teta, 2)
						- maxBraking * (
								2 * (distanceBetweenAgents - leaderSize)
								- followerSpeed * teta
								- Math.pow(leaderSpeed, 2) / maximumLeaderBraking
								)
						);
	}

	/**
	 * Maximum vehicle acceleration, in m/s-2
	 * @return maximum vehicle acceleration (m/s-2)
	 */
	public double getMaxAcceleration() {
		return maxAcceleration;
	}


	/**
	 * Maximum vehicle braking, negative, in m/s-2
	 * @return maximum vehicle braking (m/s-2)
	 */
	public double getMaxBraking() {
		return maxBraking;
	}
	
	

}
