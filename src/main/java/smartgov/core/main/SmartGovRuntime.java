package smartgov.core.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.events.EventHandler;
import smartgov.core.main.events.SimulationPaused;
import smartgov.core.main.events.SimulationResumed;
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

	// Time that correspond to a tick, in seconds
	public double tickDuration = 1.0;
	
	// Delay between each ticks in ms
	public long tickDelay = 0;
	
	private SimulationThread simulationThread;

	private Collection<EventHandler<SimulationStarted>> simulationStartedEventHandlers;
	private Collection<EventHandler<SimulationStopped>> simulationStoppedEventHandlers;
	private Collection<EventHandler<SimulationPaused>> simulationPausedEventHandlers;
	private Collection<EventHandler<SimulationResumed>> simulationResumedEventHandlers;
	private Collection<EventHandler<SimulationStep>> simulationStepEventHandlers;
	
	public SmartGovRuntime(SmartGovContext context) {
		this.context = context;
		simulationStartedEventHandlers = new ArrayList<>();
		simulationStoppedEventHandlers = new ArrayList<>();
		simulationPausedEventHandlers = new ArrayList<>();
		simulationResumedEventHandlers = new ArrayList<>();
		simulationStepEventHandlers = new ArrayList<>();
	}
	
	/**
	 * Run the simulation until stop is called.
	 */
	public void start() {
		if (run) {
			throw new IllegalStateException("A Simulation is already running in this SmartGovRuntime.");
		}
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
	
	public void pause() throws IllegalArgumentException {
		if(!run) {
			throw new IllegalStateException("No simulation running.");
		}
		logger.info("Simulation paused at " + tickCount + " ticks.");
		pause = true;
		triggerSimulationPausedListeners();
	}
	
	public void resume() throws IllegalArgumentException {
		if (!run) {
			throw new IllegalStateException("No simulation running.");
		}
		if (!pause) {
			throw new IllegalStateException("Simulation is not paused.");
		}
		logger.info("Resume simulation from " + tickCount + " ticks.");
		pause = false;
		triggerSimulationResumedListeners();
	}
	
	public void stop() {
		if(!run) {
			throw new IllegalStateException("No simulation running.");
		}
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
			logger.info("Step at " + tickCount + " ticks.");
			_step();
		}
		else {
			throw new IllegalStateException("Unavailable operation when the simulation is not stopped.");
		}
	}
	
	private void _step() {
		long begin = System.currentTimeMillis();
		for (Agent agent : context.agents.values()) {
			agent.live();
		}
		tickCount ++;
		triggerSimulationStepListeners();
		if(tickCount >= maxTicks) {
			stop();
		}
		if(tickDelay > 0) {
			long timeLeft = tickDelay - (System.currentTimeMillis() - begin);
			if (timeLeft > 0) {
				try {
					Thread.sleep(timeLeft);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				SmartGov.logger.warn("Overflow");
			}
		}
	}
	
	public int getTickCount() {
		return tickCount;
	}
	
	public double getTickDuration() {
		return tickDuration;
	}
	
	public void setTickDuration(double tickDuration) {
		this.tickDuration = tickDuration;
	}

	public double getTickDelay() {
		return tickDelay;
	}

	public void setTickDelay(long tickDelay) {
		this.tickDelay = tickDelay;
	}
	
	private class SimulationThread extends Thread {
		
		@Override
		public void run() {
			while(run) {
				_step();
				while(pause) {
					try {
						TimeUnit.MICROSECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void addSimulationStartedListener(EventHandler<SimulationStarted> listener) {
		simulationStartedEventHandlers.add(listener);
	}
	
	private void triggerSimulationStartedListeners() {
		SimulationStarted event = new SimulationStarted(maxTicks);
		for(EventHandler<SimulationStarted> listener : simulationStartedEventHandlers) {
			listener.handle(event);
		}
	}
	
	public void addSimulationStoppedListener(EventHandler<SimulationStopped> listener) {
		simulationStoppedEventHandlers.add(listener);
	}
	
	private void triggerSimulationStoppedListeners() {
		SimulationStopped event = new SimulationStopped(tickCount);
		for(EventHandler<SimulationStopped> listener : simulationStoppedEventHandlers) {
			listener.handle(event);
		}
	}
	
	public void addSimulationPausedListener(EventHandler<SimulationPaused> listener) {
		simulationPausedEventHandlers.add(listener);
	}
	
	private void triggerSimulationPausedListeners() {
		SimulationPaused event = new SimulationPaused(tickCount);
		for(EventHandler<SimulationPaused> listener : simulationPausedEventHandlers) {
			listener.handle(event);
		}
	}
	
	public void addSimulationResumedListener(EventHandler<SimulationResumed> listener) {
		simulationResumedEventHandlers.add(listener);
	}
	
	private void triggerSimulationResumedListeners() {
		SimulationResumed event = new SimulationResumed(tickCount);
		for(EventHandler<SimulationResumed> listener : simulationResumedEventHandlers) {
			listener.handle(event);
		}
	}
	
	public void addSimulationStepListener(EventHandler<SimulationStep> listener) {
		simulationStepEventHandlers.add(listener);
	}
	
	private void triggerSimulationStepListeners() {
		SimulationStep event = new SimulationStep(tickCount);
		for(EventHandler<SimulationStep> listener : simulationStepEventHandlers) {
			listener.handle(event);
		}
	}
}
