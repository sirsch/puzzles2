package software.sirsch.sa4e.puzzles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse stellt einen Integrationstest für {@link MqttSolverProgressLogger} bereit.
 *
 * @author sirsch
 * @since 22.01.2023
 */
public class MqttSolverProgressLoggerIT {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private MqttSolverProgressLogger objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new MqttSolverProgressLogger("tcp://localhost:1883");
	}

	/**
	 * Diese Methode prüft {@link MqttSolverProgressLogger#log(String)}.
	 *
	 * @throws InterruptedException wird in diesem Fall nicht erwartet
	 */
	@Test
	public void testLog() throws InterruptedException {
		this.objectUnderTest.log("Test 1");
		Thread.sleep(5000);
		this.objectUnderTest.log("Test 2");
		Thread.sleep(5000);
		this.objectUnderTest.log("Test 3");
	}
}
