package smartgov.urban.geo;

import smartgov.SmartGov;
import smartgov.core.environment.TestContext;

public class SmartGovGeoTest extends SmartGov {

	public SmartGovGeoTest() {
		super(new TestContext(SmartGovGeoTest.class.getResource("geo_test_input.properties").getFile()));
	}

}
