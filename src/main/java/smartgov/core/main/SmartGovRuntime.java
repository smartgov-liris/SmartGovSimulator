package smartgov.core.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import smartgov.core.agent.AbstractAgent;
import smartgov.core.environment.SmartGovContext;

public class SmartGovRuntime {
	
	private final Logger logger = LogManager.getLogger(SmartGovRuntime.class);

	private SmartGovContext context;
	private int tickCount = 0;
	private int maxTicks = Integer.MAX_VALUE;
	private boolean run = false;
	private boolean pause = true;
	private SimulationThread simulationThread;
	
	public SmartGovRuntime(SmartGovContext context) {
		this.context = context;
	}
	
	/**
	 * Run the simulation until stop is called.
	 */
	public void start() {
		logger.info("Start simulation");
		run = true;
		pause = false;
		simulationThread = new SimulationThread();
		simulationThread.start();
	}
	
	public void start(int ticks) {
		maxTicks = ticks;
		start();
	}
	
	public void pause() {
		pause = true;
	}
	
	public void resume() {
		pause = false;
	}
	
	public void stop() {
		logger.info("Stop simulation after " + tickCount + " ticks.");
		pause = false;
		run = false;
	}
	
	/**
	 * Performs a step, if the current simulation is running and paused.
	 */
	public void step() {
		if(run && pause) {
			_step();
		}
	}
	
	private void _step() {
		for (AbstractAgent<?> agent : context.agents.values()) {
			agent.live();
		}
		if(tickCount >= maxTicks) {
			stop();
		}
		tickCount ++;
	}
	
	public int getTickCount() {
		return tickCount;
	}
	
	private class SimulationThread extends Thread {
		
		@Override
		public void run() {
			while(run) {
				_step();
				while(pause) {
					
				}
			}
		}
	}
}
