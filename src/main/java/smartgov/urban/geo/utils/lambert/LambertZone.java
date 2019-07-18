package smartgov.urban.geo.utils.lambert;

public abstract class LambertZone extends Lambert {

	// Clarke 1880 IGN geodesic ellipsoid
	private static final double a = 6378249.2; // Semi-major axe
	private static final double b = 6356515; // Semi-minor axe
	private static double e = Math.sqrt(1 - Math.pow(b, 2) / Math.pow(a, 2)); // First eccentricity
	
	// Origin longitude
	private static final double lambda0 = (2 + 20 / 60 + 14.025 / 3600) // 2°20'14.025"
											* Math.PI / 180; // Radian
	
	public LambertZone(double phi0, double k0, double x0, double y0) {
		super(a, b, e, lambda0, phi0, k0, x0, y0);
		// TODO Auto-generated constructor stub
	}

}
