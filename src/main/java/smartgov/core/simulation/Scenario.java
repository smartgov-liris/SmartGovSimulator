package smartgov.core.simulation;

import smartgov.core.environment.SmartGovContext;

public abstract class Scenario {
	
	private SmartGovContext<?, ?, ?> context;

	/**
	 * Add elements to context and instantiates agents.
	 * @param context Repast Simphony context.
	 * @return Updated context used by SmartGov simulator
	 */
	public abstract void loadWorld(SmartGovContext<?, ?, ?> context);
}
