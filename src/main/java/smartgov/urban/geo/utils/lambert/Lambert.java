package smartgov.urban.geo.utils.lambert;

import org.locationtech.jts.geom.Coordinate;

import smartgov.urban.geo.utils.LatLon;
import smartgov.urban.geo.utils.Projection;

public abstract class Lambert implements Projection {

	protected double a;
	protected double b;
	protected double e;
	
	protected double lambda0;
	
	
	protected double phi0;
	protected double k0;
	protected double x0;
	protected double y0;
	
	protected static final double epsilon = 1E-11;
	
	// Computed parameters
	// First parameters set
	private double lambdaC;
	private double n;
	private double C;
	private double Xs;
	private double Ys;
	
	public Lambert(double a, double b, double e, double lambda0, double phi0, double k0, double x0, double y0) {
		super();
		this.a = a;
		this.b = b;
		this.e = e;
		this.lambda0 = lambda0;
		this.phi0 = phi0;
		this.k0 = k0;
		this.x0 = x0;
		this.y0 = y0;
		
		lambdaC = lambda0;
		n = Math.sin(phi0);
		C = C(k0, phi0, a, e);
		Xs = x0;
		Ys = Ys(y0, k0, phi0, a, e);
	}
	
	// Grande normale (ALG0021)
	static double N(double phi, double a, double e) {
		return a / Math.sqrt(1 - Math.pow(e, 2) * Math.pow(Math.sin(phi), 2));
	}
	//
	
	// Projection parameters (ALG0019)
	static double C(double k0, double phi0, double a, double e) {
		return k0 * N(phi0, a, e) * (1 / Math.tan(phi0)) * Math.exp(Math.sin(phi0) * L(phi0, e));
	}
	
	
	static double Ys(double Y0, double k0, double phi0, double a, double e) {
		return Y0 + k0 * N(phi0, a, e) * 1 / Math.tan(phi0);
	}
	//
	
	// Isometric latitude (ALG0001)
	static double L(double phi, double e) {
		return Math.log(
				Math.tan(Math.PI / 4 + phi / 2) * Math.pow((1 - e * Math.sin(phi)) / (1 + e * Math.sin(phi)), e / 2)
				);
	}
	//
	
	// Isometric latitude inverse (ALG0002)
	static double phi(double L, double e) {
		double phi = 2 * Math.atan(Math.exp(L)) - Math.PI / 2;
		double phi_i = phi_i(L, phi, e);
		while(Math.abs(phi_i - phi) >= epsilon) {
			phi = phi_i;
			phi_i = phi_i(L, phi_i, e);
		}
		return phi_i;
	}
	
	private static final double phi_i(double L, double phi, double e) {
		return 2 * Math.atan(
					Math.pow((1 + e * Math.sin(phi)) / (1 - e * Math.sin(phi)), e / 2)
					* Math.exp(L)
				) - Math.PI / 2;
	}
	//
	
	// Other variables used in ALG0004
	private static double R(double Xs, double Ys, double x, double y) {
		return Math.sqrt(Math.pow(x - Xs, 2) + Math.pow(y - Ys, 2));
	}
	
	private static double gamma(double Xs, double Ys, double x, double y) {
		return Math.atan((x - Xs) / (Ys - y));
	}
	
	private static double lambda(double lambdaC, double gamma, double n) {
		return lambdaC + gamma / n;
	}
	//
	
	// Conversion to latLon (ALG0004)
	static Coordinate LatLon(double n, double e, double c, double lambdaC, double Xs, double Ys, double x, double y) {
		double R = R(Xs, Ys, x, y);
		double gamma = gamma(Xs, Ys, x, y);
		double lambda = lambda(lambdaC, gamma, n);
		double L = - (1 / n) * Math.log(Math.abs(R / c));
		double phi = phi(L, e);
		return new Coordinate(phi, lambda);
	}

	
	@Override
	public Coordinate project(LatLon geoPoint) {
		// TODO might be implemented later, just for fun.
		return null;
	}
	
	/**
	 * Converts Lambert II X/Y coordinates to Lat/Lon coordinates.
	 * 
	 * @param point lambertCoordinate X/Y Lambert II coordinates
	 * @return Lat/Lon coordinates
	 */
	@Override
	public LatLon unproject(Coordinate point) {
		double x = point.x;
		double y = point.y;
		Coordinate rad =  LatLon(n, e, C, lambdaC, Xs, Ys, x, y);
		return new LatLon(180 * rad.x / Math.PI, 180 * rad.y / Math.PI);
	}
}
