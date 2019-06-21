package smartgov.core.main;

import smartgov.SmartGov;
import smartgov.core.environment.SmartGovContext;

/**
 * Class used to <em>build</em> the context depending on the loaded scenario.
 *
 * @author pbreugnot
 *
 */
public class SimulationBuilder {

	private SmartGovContext context;
	
	/**
	 * SimulationBuilder constructor.
	 *
	 * @param context current context.
	 */
	public SimulationBuilder(SmartGovContext context) {
		this.context = context;
	}

	/**
	 * Builds the current context.
	 *
	 * <p>
	 * Clears the current context, and then calls the
	 * {@link smartgov.core.simulation.Scenario#loadWorld loadWorld}
	 * method of the loaded scenario, if it exists.
	 * </p>
	 *
	 * <p>
	 * Notice that this function can be called several times in
	 * the same application instance. Typically, it could be
	 * called after a simulation has stopped or ended to 
	 * re-initialize the scenario (and the context) at its original
	 * state.
	 * </p>
	 */
	public void build() {
		long beginTime = System.currentTimeMillis();

		context.clear();

		if (context.getScenario() != null) {
			SmartGov.logger.info("Loading World for " + context.getScenario().getClass().getSimpleName());
			context.getScenario().loadWorld(context);
			SmartGov.logger.info("Time to process simulation creation: " + (System.currentTimeMillis() - beginTime) + " ms.");
		}
		else {
			SmartGov.logger.error("Scenario not found");
		}
	}
	
	/**
	 * Current context.
	 *
	 * @return current smartGov context
	 */
	public SmartGovContext getContext() {
		return context;
	} 

}
