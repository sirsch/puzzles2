package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link RequestSolvePuzzleCommand} bereit.
 *
 * @author sirsch
 * @since 31.12.2022
 */
public class RequestSolvePuzzleCommandTest {

	/**
	 * Dieses Feld soll den Mock für {@link PrintStream} für die Ausgabe enthalten.
	 */
	private PrintStream out;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzlePrinter} enthalten.
	 */
	private PuzzlePrinter puzzlePrinter;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleGenerator} enthalten.
	 */
	private PuzzleGenerator puzzleGenerator;

	/**
	 * Dieses Feld soll den Mock für die Fabrik für {@link PuzzleGenerator} enthalten.
	 */
	private Factory<PuzzleGenerator> puzzleGeneratorFactory;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleSolverClient} enthalten.
	 */
	private PuzzleSolverClient puzzleSolverClient;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleSolverClientFactory} enthalten.
	 */
	private PuzzleSolverClientFactory puzzleSolverClientFactory;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private RequestSolvePuzzleCommand objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.out = mock(PrintStream.class);
		this.puzzlePrinter = mock(PuzzlePrinter.class);
		this.puzzleGenerator = mock(PuzzleGenerator.class);
		this.puzzleGeneratorFactory = mock(Factory.class);
		this.puzzleSolverClient = mock(PuzzleSolverClient.class);
		this.puzzleSolverClientFactory = mock(PuzzleSolverClientFactory.class);
		when(this.puzzleGeneratorFactory.create()).thenReturn(this.puzzleGenerator);
		when(this.puzzleSolverClientFactory.create("testhost", 42))
				.thenReturn(this.puzzleSolverClient);

		this.objectUnderTest = new RequestSolvePuzzleCommand(
				this.out,
				this.puzzlePrinter,
				this.puzzleGeneratorFactory,
				this.puzzleSolverClientFactory);
	}

	/**
	 * Diese Methode prüft {@link RequestSolvePuzzleCommand#execute(String...)}.
	 */
	@Test
	public void testExecute() {
		Puzzle puzzle = mock(Puzzle.class);
		Symbol symbol1 = mock(Symbol.class);
		Symbol symbol2 = mock(Symbol.class);

		when(symbol1.getId()).thenReturn(1);
		when(symbol1.getIconCodePoint()).thenReturn((int) 'A');
		when(symbol2.getId()).thenReturn(2);
		when(symbol2.getIconCodePoint()).thenReturn((int) 'B');
		when(puzzle.getSymbols()).thenReturn(List.of(symbol1, symbol2));
		when(this.puzzleGenerator.generate(3)).thenReturn(puzzle);
		when(this.puzzleSolverClient.solvePuzzle(puzzle))
				.thenReturn(Map.of(1, 1, 2, 2));

		this.objectUnderTest.execute("request-solve-puzzle", "testhost", "42", "3");

		verify(this.puzzlePrinter).print(puzzle);
		verify(this.out).println("Solution found:");
		verify(this.out).println("Symbol A ID: 1, digit value: 1");
		verify(this.out).println("Symbol B ID: 2, digit value: 2");
	}

	/**
	 * Diese Methode prüft {@link RequestSolvePuzzleCommand#execute(String...)}, ohne den optionalen
	 * Parameter numberOfDigits.
	 */
	@Test
	public void testExecuteDefaultNumberOfDigits() {
		Puzzle puzzle = mock(Puzzle.class);
		Symbol symbol1 = mock(Symbol.class);
		Symbol symbol2 = mock(Symbol.class);

		when(symbol1.getId()).thenReturn(1);
		when(symbol1.getIconCodePoint()).thenReturn((int) 'A');
		when(symbol2.getId()).thenReturn(2);
		when(symbol2.getIconCodePoint()).thenReturn((int) 'B');
		when(puzzle.getSymbols()).thenReturn(List.of(symbol1, symbol2));
		when(this.puzzleGenerator.generate(2)).thenReturn(puzzle);
		when(this.puzzleSolverClient.solvePuzzle(puzzle))
				.thenReturn(Map.of(1, 1, 2, 2));

		this.objectUnderTest.execute("request-solve-puzzle", "testhost", "42");

		verify(this.puzzlePrinter).print(puzzle);
		verify(this.out).println("Solution found:");
		verify(this.out).println("Symbol A ID: 1, digit value: 1");
		verify(this.out).println("Symbol B ID: 2, digit value: 2");
	}

	/**
	 * Diese Methode prüft {@link RequestSolvePuzzleCommand#execute(String...)}, wenn der Parameter
	 * serverHost fehlt.
	 */
	@Test
	public void testExecuteMissingServerHost() {
		this.objectUnderTest = new RequestSolvePuzzleCommand();

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.execute("request-solve-puzzle"));
	}

	/**
	 * Diese Methode prüft {@link RequestSolvePuzzleCommand#execute(String...)}, wenn der Parameter
	 * serverPort fehlt.
	 */
	@Test
	public void testExecuteMissingServerPort() {
		this.objectUnderTest = new RequestSolvePuzzleCommand();

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.execute("request-solve-puzzle", "testhost"));
	}
}
