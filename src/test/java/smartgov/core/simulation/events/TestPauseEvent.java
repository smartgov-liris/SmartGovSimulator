package smartgov.core.simulation.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import smartgov.SmartGov;
import smartgov.SmartGovTest;
import smartgov.core.events.EventHandler;
import smartgov.core.simulation.events.SimulationPaused;
import smartgov.core.simulation.events.SimulationStep;

public class TestPauseEvent {
	
	@Test
	public void testPauseTriggered() throws InterruptedException {
		SmartGovTest.loadSmartGov();
		
		EventTriggeredChecker checker = new EventTriggeredChecker();
		
		SmartGov.getRuntime().addSimulationPausedListener(new EventHandler<SimulationPaused>() {

			@Override
			public void handle(SimulationPaused event) {
				checker.eventTriggered = true;
				checker.buildJson(event);
				SmartGov.getRuntime().resume();
			}
			
		});
		
		SmartGov.getRuntime().addSimulationStepListener(new EventHandler<SimulationStep>() {

			@Override
			public void handle(SimulationStep event) {
				if(event.getTick() == 5) {
					SmartGov.getRuntime().pause();
				}
				
			}
			
		});
		
		SmartGov.getRuntime().start(100);
		
		while(SmartGov.getRuntime().isRunning()) {
			TimeUnit.MICROSECONDS.sleep(10);
		}
		
		assertThat(
				checker.eventTriggered,
				equalTo(true)
				);
		
		assertThat(
				checker.json,
				equalTo("{\"pause\":{\"tick\":5,\"date\":{\"day\":0,\"weekDay\":\"MONDAY\",\"hour\":\"0:0:5.0\"}}}")
				);
	}

}
