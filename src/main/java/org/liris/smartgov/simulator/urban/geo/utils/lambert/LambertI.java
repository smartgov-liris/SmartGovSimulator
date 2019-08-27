package org.liris.smartgov.simulator.urban.geo.utils.lambert;

public class LambertI extends LambertZone {
	
	// Origin latitude
	private static final double phi0 = 55 * Math.PI / 200; // 52 grades (rad)
	
	// Scale factor
	private static final double k0 = 0.99987734;
	
	// Origin translation
	private static final double X0 = 600000;
	private static final double Y0 = 1200000;
	
	public LambertI() {
		super(phi0, k0, X0, Y0);
	}
}
