package smartgov.urban.geo.utils.lambert;

public class LambertIV extends LambertZone {

	// Origin latitude
	private static final double phi0 = 46.85 * Math.PI / 200; // 52 grades (rad)
	
	// Scale factor
	private static final double k0 = 0.99994471;
	
	// Origin translation
	private static final double X0 = 234.358;
	private static final double Y0 = 4185861.369;
	
	public LambertIV() {
		super(phi0, k0, X0, Y0);
	}
}
