package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link RunServerCommand} bereit.
 *
 * @author sirsch
 * @since 29.12.2022
 */
public class RunServerCommandTest {

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleSolverServer} enthalten.
	 */
	private PuzzleSolverServer puzzleSolverServer;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleSolverServerFactory} enthalten.
	 */
	private PuzzleSolverServerFactory puzzleSolverServerFactory;

	/**
	 * Dieses Feld soll den Mock für {@link PrintStream} enthalten.
	 */
	private PrintStream out;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private RunServerCommand objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.puzzleSolverServer = mock(PuzzleSolverServer.class);
		this.puzzleSolverServerFactory = mock(PuzzleSolverServerFactory.class);
		this.out = mock(PrintStream.class);
		when(this.puzzleSolverServerFactory.create(12345)).thenReturn(this.puzzleSolverServer);

		this.objectUnderTest = new RunServerCommand(this.puzzleSolverServerFactory, this.out);
	}

	/**
	 * Diese Methode prüft {@link RunServerCommand#RunServerCommand()}.
	 */
	@Test
	public void testConstructor() {
		this.objectUnderTest = new RunServerCommand();

		assertThrows(IllegalArgumentException.class, () -> this.objectUnderTest.execute());
	}

	/**
	 * Diese Methode prüft {@link RunServerCommand#execute(String...)}.
	 */
	@Test
	public void testExecute() {
		InOrder orderVerifier = inOrder(this.puzzleSolverServer, this.out);

		this.objectUnderTest.execute("run-server", "12345");

		orderVerifier.verify(this.out).println("Starting server...");
		orderVerifier.verify(this.puzzleSolverServer).run();
		orderVerifier.verify(this.out).println("Server stopped!");
		orderVerifier.verifyNoMoreInteractions();
	}
}