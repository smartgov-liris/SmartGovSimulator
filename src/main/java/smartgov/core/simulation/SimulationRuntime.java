package smartgov.core.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import smartgov.SmartGov;
import smartgov.core.agent.core.Agent;
import smartgov.core.environment.SmartGovContext;
import smartgov.core.events.EventHandler;
import smartgov.core.simulation.events.SimulationPaused;
import smartgov.core.simulation.events.SimulationResumed;
import smartgov.core.simulation.events.SimulationStarted;
import smartgov.core.simulation.events.SimulationStep;
import smartgov.core.simulation.events.SimulationStopped;
import smartgov.core.simulation.time.Clock;

/**
 * Class used to run the simulation and update the context elements.
 */
public class SimulationRuntime {
	
	private final Logger logger = LogManager.getLogger(SimulationRuntime.class);

	private SmartGovContext context;
	private int tickCount = 0;
	private int maxTicks = Integer.MAX_VALUE;
	private boolean run = false;
	private boolean pause = true;

	// Time that correspond to a tick, in seconds
	private double tickDuration = 1.0;
	
	// Delay between each ticks in ms
	private long tickDelay = 0;
	
	// Simulation clock
	private Clock clock;
	
	private SimulationThread simulationThread;

	private Collection<EventHandler<SimulationStarted>> simulationStartedEventHandlers;
	private Collection<EventHandler<SimulationStopped>> simulationStoppedEventHandlers;
	private Collection<EventHandler<SimulationPaused>> simulationPausedEventHandlers;
	private Collection<EventHandler<SimulationResumed>> simulationResumedEventHandlers;
	private Collection<EventHandler<SimulationStep>> simulationStepEventHandlers;
	
	/**
	 * SimulationRuntime constructor.
	 *
	 * @param context Current context
	 */
	public SimulationRuntime(SmartGovContext context) {
		this.context = context;
		simulationStartedEventHandlers = new ArrayList<>();
		simulationStoppedEventHandlers = new ArrayList<>();
		simulationPausedEventHandlers = new ArrayList<>();
		simulationResumedEventHandlers = new ArrayList<>();
		simulationStepEventHandlers = new ArrayList<>();
		clock = new Clock();
	}
	
	/**
	 * Runs the simulation until stop is called.
	 *
	 * @throws IllegalStateException if a simulation is already running.
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
	
	/**
	 * Runs the simulation for the specified number of ticks.
	 *
	 * @param ticks number of ticks to run
	 * @throws IllegalStateException if a simulation is already running.
	 */
	public void start(int ticks) {
		maxTicks = ticks;
		start();
	}
	
	/**
	 * Pause the simulation, if it is running.
	 *
	 * @throws IllegalStateException if no simulation is running.
	 */
	public void pause() throws IllegalArgumentException {
		if(!run) {
			throw new IllegalStateException("No simulation running.");
		}
		logger.info("Simulation paused at " + tickCount + " ticks.");
		pause = true;
		triggerSimulationPausedListeners();
	}
	
	/**
	 * Resume a paused simulation.
	 *
	 * @throws IllegalArgumentException if no simulation is running or if
	 * it's not paused.
	 */
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
	
	/**
	 * Stop a running simulation.
	 * 
	 * @throws IllegalStateException if no simulation is running.
	 */
	public void stop() {
		if(!run) {
			throw new IllegalStateException("No simulation running.");
		}
		logger.info("Stop simulation after " + tickCount + " ticks.");
		pause = false;
		run = false;
		triggerSimulationStoppedListeners();
		clock.reset();
	}
	
	/**
	 * Check if the simulation is still running.
	 *
	 * Notice that a paused simulation is considered as <em>running</em>.
	 *
	 * @return true if and only if the simulation is running.
	 */
	public boolean isRunning() {
		return run;
	}
	
	/**
	 * Performs a step, if the current simulation is running and paused.
	 * 
	 * @throws IllegalArgumentException if no simulation is running or if
	 * it's not paused.
	 */
	public void step() {
		if(!run) {
			throw new IllegalStateException("No simulation running.");
		}
		if(!pause) {
			throw new IllegalStateException("The simulation is not paused.");
		}

		logger.info("Step at " + tickCount + " ticks.");
		_step();
	}
	
	private void _step() {
		long begin = System.currentTimeMillis();
		for (Agent<?> agent : context.agents.values()) {
			agent.live();
		}
		tickCount ++;
		clock.increment(tickDuration);

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

	/**
	 * Gets current tick count.
	 *
	 * @return current tick count
	 */
	public int getTickCount() {
		return tickCount;
	}
	
	/**
	 * Gets tick duration, in seconds.
	 *
	 * <p>
	 * The tick duration represents the real amount
	 * of time a tick represents, and so can be used to
	 * compute speeds for example.
	 * </p>
	 *
	 * @return current tick duration
	 */
	public double getTickDuration() {
		return tickDuration;
	}
	
	/**
	 * Sets tick duration, in seconds.
	 *
	 * <p>
	 * Default value is set to 1.0.
	 * </p>
	 *
	 * @param tickDuration new tick duration
	 */
	public void setTickDuration(double tickDuration) {
		this.tickDuration = tickDuration;
	}

	/**
	 * Gets tick delay, in microseconds.
	 *
	 * <p>
	 * The tick delay represents the duration between each simulation
	 * steps, in the meaning of real time simulation visualization.
	 * </p>
	 *
	 * <p>
	 * When set to 0 (as it is by default) or to a negative value, the
	 * simulation runs as fast as possible without any delay.
	 * </p>
	 *
	 * @return tick delay, in microseconds
	 */
	public double getTickDelay() {
		return tickDelay;
	}

	/**
	 * Sets tick delay.
	 *
	 * Default value is set to 0.
	 *
	 * @param tickDelay new tick delay
	 */
	public void setTickDelay(long tickDelay) {
		this.tickDelay = tickDelay;
	}
	
	/**
	 * Current simulation clock
	 * @return simulation clock
	 */
	public Clock getClock() {
		return clock;
	}
	
	/**
	 * Blocking function to wait until the simulation has stopped.
	 * 
	 * @throws InterruptedException if interrupted while sleeping 
	 */
	public void waitUntilSimulatioEnd() throws InterruptedException {
		while(isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
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
	
	/**
	 * Adds an <code>EventHandler</code> for <code>SimulationStarted</code>
	 * events.
	 *
	 * @param listener event handler to add
	 */
	public void addSimulationStartedListener(EventHandler<SimulationStarted> listener) {
		simulationStartedEventHandlers.add(listener);
	}
	
	private void triggerSimulationStartedListeners() {
		SimulationStarted event = new SimulationStarted(maxTicks);
		for(EventHandler<SimulationStarted> listener : simulationStartedEventHandlers) {
			listener.handle(event);
		}
	}
	
	/**
	 * Adds an <code>EventHandler</code> for <code>SimulationStopped</code>
	 * events.
	 * 
	 * @param listener event handler to add
	 */
	public void addSimulationStoppedListener(EventHandler<SimulationStopped> listener) {
		simulationStoppedEventHandlers.add(listener);
	}
	
	private void triggerSimulationStoppedListeners() {
		SimulationStopped event = new SimulationStopped(tickCount, clock.time());
		for(EventHandler<SimulationStopped> listener : simulationStoppedEventHandlers) {
			listener.handle(event);
		}
	}
	
	/**
	 * Adds an <code>EventHandler</code> for <code>SimulationPaused</code>
	 * events.
	 * 
	 * @param listener event handler to add
	 */
	public void addSimulationPausedListener(EventHandler<SimulationPaused> listener) {
		simulationPausedEventHandlers.add(listener);
	}
	
	private void triggerSimulationPausedListeners() {
		SimulationPaused event = new SimulationPaused(tickCount, clock.time());
		for(EventHandler<SimulationPaused> listener : simulationPausedEventHandlers) {
			listener.handle(event);
		}
	}
	
	/**
	 * Adds an <code>EventHandler</code> for <code>SimulationResumed</code>
	 * events.
	 * 
	 * @param listener event handler to add
	 */
	public void addSimulationResumedListener(EventHandler<SimulationResumed> listener) {
		simulationResumedEventHandlers.add(listener);
	}
	
	private void triggerSimulationResumedListeners() {
		SimulationResumed event = new SimulationResumed(tickCount, clock.time());
		for(EventHandler<SimulationResumed> listener : simulationResumedEventHandlers) {
			listener.handle(event);
		}
	}
	
	/**
	 * Adds an <code>EventHandler</code> for <code>SimulationStep</code>
	 * events.
	 * 
	 * @param listener event handler to add
	 */
	public void addSimulationStepListener(EventHandler<SimulationStep> listener) {
		simulationStepEventHandlers.add(listener);
	}
	
	private void triggerSimulationStepListeners() {
		SimulationStep event = new SimulationStep(tickCount, clock.time());
		for(EventHandler<SimulationStep> listener : simulationStepEventHandlers) {
			listener.handle(event);
		}
	}
}
