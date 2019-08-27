package org.liris.smartgov.simulator.urban.geo;

import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.environment.TestContext;

public class SmartGovGeoTest extends SmartGov {

	public SmartGovGeoTest() {
		super(new TestContext(SmartGovGeoTest.class.getResource("geo_test_input.properties").getFile()));
	}

}
