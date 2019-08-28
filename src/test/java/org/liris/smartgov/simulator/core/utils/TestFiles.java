package org.liris.smartgov.simulator.core.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;

import org.junit.Test;
import org.liris.smartgov.simulator.core.environment.TestContext;
import org.liris.smartgov.simulator.core.environment.TestSmartGovContext;

public class TestFiles {

	@Test
	public void testLoadFileFromConfigFile() {
		TestContext context = TestSmartGovContext.loadTestContext();
		
		String loadedPath = (String) context.getFileLoader().load("outputFolder").getAbsolutePath();
		String expectedPath = new File(TestSmartGovContext.class.getResource("output").getFile()).getAbsolutePath();
		/*
		 * Loaded output folder should be relative to the config file.
		 */
		assertThat(
				loadedPath,
				equalTo(expectedPath)
				);
	}
}
