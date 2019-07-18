package smartgov.urban.geo.utils.lambert;


public class LambertII extends LambertZone {

	// Origin latitude
	private static final double phi0 = 52 * Math.PI / 200; // 52 grades (rad)
	
	// Scale factor
	private static final double k0 = 0.99987742;
	
	// Origin translation
	private static final double X0 = 600000;
	private static final double Y0 = 2200000;
	
	public LambertII() {
		super(phi0, k0, X0, Y0);
	}
	
}

