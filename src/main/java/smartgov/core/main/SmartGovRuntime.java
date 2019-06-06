package smartgov.core.main;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.events.EventHandler;
import smartgov.core.main.events.SimulationStarted;
import smartgov.core.main.events.SimulationStep;
import smartgov.core.main.events.SimulationStopped;

public class SmartGovRuntime {
	
	private final Logger logger = LogManager.getLogger(SmartGovRuntime.class);

	private SmartGovContext context;
	private int tickCount = 0;
	private int maxTicks = Integer.MAX_VALUE;
	private boolean run = false;
	private boolean pause = true;
	private SimulationThread simulationThread;

	private Collection<EventHandler<SimulationStarted>> simulationStartedEventHandlers;
	private Collection<EventHandler<SimulationStopped>> simulationStoppedEventHandlers;
	private Collection<EventHandler<SimulationStep>> simulationStepEventHandlers;
	
	public SmartGovRuntime(SmartGovContext context) {
		this.context = context;
		simulationStartedEventHandlers = new ArrayList<>();
		simulationStoppedEventHandlers = new ArrayList<>();
		simulationStepEventHandlers = new ArrayList<>();
	}
	
	/**
	 * Run the simulation until stop is called.
	 */
	public void start() {
		run = true;
		pause = false;
		tickCount = 0;
		logger.info("Start simulation");
		simulationThread = new SimulationThread();
		simulationThread.start();
		triggerSimulationStartedListeners();
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
		triggerSimulationStoppedListeners();
	}
	
	public boolean isRunning() {
		return run;
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
		for (Agent agent : context.agents.values()) {
			agent.live();
		}
		triggerSimulationStepListeners();
		tickCount ++;
		if(tickCount >= maxTicks) {
			stop();
		}
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
	
	public void addSimulationStartedListener(EventHandler<SimulationStarted> listener) {
		simulationStartedEventHandlers.add(listener);
	}
	
	private void triggerSimulationStartedListeners() {
		SimulationStarted event = new SimulationStarted();
		for(EventHandler<SimulationStarted> listener : simulationStartedEventHandlers) {
			listener.handle(event);
		}
	}
	
	public void addSimulationStoppedListener(EventHandler<SimulationStopped> listener) {
		simulationStoppedEventHandlers.add(listener);
	}
	
	private void triggerSimulationStoppedListeners() {
		SimulationStopped event = new SimulationStopped();
		for(EventHandler<SimulationStopped> listener : simulationStoppedEventHandlers) {
			listener.handle(event);
		}
	}
	
	public void addSimulationStepListener(EventHandler<SimulationStep> listener) {
		simulationStepEventHandlers.add(listener);
	}
	
	private void triggerSimulationStepListeners() {
		SimulationStep event = new SimulationStep();
		for(EventHandler<SimulationStep> listener : simulationStepEventHandlers) {
			listener.handle(event);
		}
	}
}
