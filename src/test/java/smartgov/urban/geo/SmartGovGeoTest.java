package smartgov.urban.geo;

import smartgov.SmartGov;
import smartgov.urban.geo.environment.GeoTestContext;

public class SmartGovGeoTest extends SmartGov {

	public SmartGovGeoTest() {
		super(new GeoTestContext(SmartGovGeoTest.class.getResource("geo_test_input.properties").getFile()));
	}

}
