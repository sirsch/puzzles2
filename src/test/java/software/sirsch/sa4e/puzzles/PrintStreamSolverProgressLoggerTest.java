package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Diese Klasse stellt Tests für {@link PrintStreamSolverProgressLogger} bereit.
 *
 * @author sirsch
 * @since 20.01.2023
 */
public class PrintStreamSolverProgressLoggerTest {

	/**
	 * Dieses Feld soll den Mock für die Ausgabe enthalten.
	 */
	private PrintStream out;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PrintStreamSolverProgressLogger objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.out = mock(PrintStream.class);

		this.objectUnderTest = new PrintStreamSolverProgressLogger(this.out);
	}

	/**
	 * Diese Methode prüft {@link PrintStreamSolverProgressLogger#log(String)}.
	 */
	@Test
	public void testLog() {
		this.objectUnderTest.log("testMessage");

		verify(this.out).println("testMessage");
	}

	/**
	 * Diese Methode prüft {@link PrintStreamSolverProgressLogger#close()}.
	 */
	@Test
	public void testClose() {
		this.objectUnderTest.close();

		verify(this.out).close();
	}
}
