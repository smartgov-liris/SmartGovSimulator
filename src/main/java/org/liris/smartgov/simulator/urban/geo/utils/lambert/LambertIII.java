package org.liris.smartgov.simulator.urban.geo.utils.lambert;

public class LambertIII extends LambertZone {

	// Origin latitude
	private static final double phi0 = 49 * Math.PI / 200; // 52 grades (rad)
	
	// Scale factor
	private static final double k0 = 0.99987750;
	
	// Origin translation
	private static final double X0 = 600000;
	private static final double Y0 = 3200000;
	
	public LambertIII() {
		super(phi0, k0, X0, Y0);
	}
}
