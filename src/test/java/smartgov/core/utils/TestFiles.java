package smartgov.core.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import smartgov.core.environment.TestContext;
import smartgov.core.environment.TestSmartGovContext;

public class TestFiles {

	@Test
	public void testLoadFileFromConfigFile() {
		TestContext context = TestSmartGovContext.loadTestContext();
		
		/*
		 * Loaded output folder should be relative to the config file.
		 */
		assertThat(
				(String) context.getFiles().getFile("outputFolder"),
				equalTo(TestSmartGovContext.class.getResource("output").getFile())
				);
	}
}
