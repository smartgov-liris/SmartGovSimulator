package smartgov.core.main;

import smartgov.core.environment.SmartGovContext;

/**
 * Abstract generic main class of SmartGov. Loads a scenario and the clock singleton and GUI.
 * 
 * @param <T> The environment type used in the simulation. The Environment instantiation should be 
 * handled by the constructors of implementing classes.
 * 
 * @author Simon
 *
 */
public class SimulationBuilder {
	
	// Time that correspond to a tick, in seconds
	public static final double TICK_DURATION = 1.0;

	private SmartGovContext context;

	public SimulationBuilder(SmartGovContext context) {
		this.context = context;
		long beginTime = System.currentTimeMillis();

		context.clear();

		context.setScenario(context.loadScenario((String) context.getConfig().get("scenario")));

		if (context.getScenario() != null) {
			System.out.println("Loading scenario " + context.getScenario().getClass().getSimpleName());
			context.getScenario().loadWorld(context);
			System.out.println("Time to process simulation creation: " + (System.currentTimeMillis() - beginTime) + " ms.");
		}
		else {
			System.out.println("Scenario not found");
		}
	}
	
	public SmartGovContext getContext() {
		return context;
	}

	/**
	 * Add custom user panel to track agent informations
	 */
//	private JPanel addCustomPanel(){
//		UserPanel panel = new UserPanel();
//		return panel.createPanel();
//	}

	/**
	 * Reset User Panel if one already exist
	 */
//	private void refreshUserPanel(){
//		JPanel userPanel = addCustomPanel();
//		if(!RSApplication.getRSApplicationInstance().hasCustomUserPanelDefined()){
//			RSApplication.getRSApplicationInstance().addCustomUserPanel(userPanel);
//		} else {
//			RSApplication.getRSApplicationInstance().removeCustomUserPanel();
//			RSApplication.getRSApplicationInstance().addCustomUserPanel(userPanel);
//		}
//	}

	/**
	 * Reset clock if one already exist
	 */
//	private void refreshClock(){
//		if(ClockSingleton.getInstance() != null){
//			ClockSingleton.resetSingleton();
//		}
//	}

}
