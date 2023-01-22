package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Diese Klasse stellt Tests für {@link SolverProgressLoggerFactory} bereit.
 *
 * @author sirsch
 * @since 22.01.2023
 */
public class SolverProgressLoggerFactoryTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private SolverProgressLoggerFactory objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new SolverProgressLoggerFactory();
	}

	/**
	 * Diese Methode prüft {@link SolverProgressLoggerFactory#createStdoutLogger()}.
	 */
	@Test
	public void testCreateStdoutLogger() {
		PrintStream out = mock(PrintStream.class);
		PrintStream defaultOut = System.out;
		SolverProgressLogger result;

		try {
			System.setOut(out);

			result = this.objectUnderTest.createStdoutLogger();

			assertNotNull(result);
			result.log("testMessage");
			verify(out).println("testMessage");
		} finally {
			System.setOut(defaultOut);
		}
	}
}