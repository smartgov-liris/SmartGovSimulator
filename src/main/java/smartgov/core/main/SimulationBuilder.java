package smartgov.core.main;

import smartgov.SmartGov;
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

	private SmartGovContext context;

	public SmartGovContext build(SmartGovContext context) {
		this.context = context;
		long beginTime = System.currentTimeMillis();

		context.clear();

		context.setScenario(context.loadScenario((String) context.getConfig().get("scenario")));

		if (context.getScenario() != null) {
			SmartGov.logger.info("Loading World for " + context.getScenario().getClass().getSimpleName());
			context.getScenario().loadWorld(context);
			SmartGov.logger.info("Time to process simulation creation: " + (System.currentTimeMillis() - beginTime) + " ms.");
		}
		else {
			SmartGov.logger.error("Scenario not found");
		}
		return context;
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
