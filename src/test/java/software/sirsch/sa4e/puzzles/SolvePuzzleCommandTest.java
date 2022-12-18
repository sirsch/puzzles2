package software.sirsch.sa4e.puzzles;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;

import javax.annotation.Nonnull;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

/**
 * Diese Klasse stellt Tests für {@link SolvePuzzleCommand} bereit.
 *
 * @author sirsch
 * @since 17.12.2022
 */
public class SolvePuzzleCommandTest {

	/**
	 * Dieses Feld muss die Datei zum Testen enthalten.
	 */
	private File tempFile;

	/**
	 * Dieses Feld soll den Mock für den {@link PrintStream} out enthalten.
	 */
	private PrintStream out;

	/**
	 * Dieses Feld soll den Mock für den {@link PuzzlePrinter} enthalten.
	 */
	private PuzzlePrinter puzzlePrinter;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private SolvePuzzleCommand objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() throws IOException {
		this.tempFile = File.createTempFile("tempFile", ".test");
		this.out = mock(PrintStream.class);
		this.puzzlePrinter = mock(PuzzlePrinter.class);

		this.objectUnderTest = new SolvePuzzleCommand(this.out, this.puzzlePrinter);
	}

	/**
	 * Diese Methode räumt die Testumgebung nach jedem Testfall auf.
	 */
	@AfterEach
	public void tearDown() {
		assertTrue(this.tempFile.delete(), "Die Temp-Datei konnte nicht gelöscht werden!");
	}

	/**
	 * Diese Methode prüft {@link SolvePuzzleCommand#execute(String...)} mit einem einfachen Puzzle.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testExecuteSimplePuzzle() throws IOException {
		InOrder orderVerifier = inOrder(this.out, this.puzzlePrinter);

		this.writePuzzle(this.generateSimplePuzzle());

		this.objectUnderTest.execute(
				SolvePuzzleCommand.COMMAND_NAME,
				this.tempFile.getAbsolutePath());

		orderVerifier.verify(this.puzzlePrinter).print(notNull());
		orderVerifier.verify(this.out).println();
		orderVerifier.verify(this.out).println("Solution found:");
		orderVerifier.verify(this.out).println("Symbol 0 ID: 0, digit value: 0");
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode prüft {@link SolvePuzzleCommand#execute(String...)} mit einem unlösbaren
	 * Puzzle.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testExecuteUnsolvablePuzzle() throws IOException {
		InOrder orderVerifier = inOrder(this.out, this.puzzlePrinter);

		this.writePuzzle(this.generateUnsolvablePuzzle());

		this.objectUnderTest.execute(
				SolvePuzzleCommand.COMMAND_NAME,
				this.tempFile.getAbsolutePath());

		orderVerifier.verify(this.puzzlePrinter).print(notNull());
		orderVerifier.verify(this.out).println();
		orderVerifier.verify(this.out).println("No solution found!");
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode schreibt ein {@link Puzzle} als Protobuf in die {@link #tempFile}.
	 *
	 * @param puzzle das zu schreibende Puzzle
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	private void writePuzzle(@Nonnull final Puzzle puzzle) throws IOException {
		FileUtils.writeByteArrayToFile(
				this.tempFile,
				new Puzzle2ProtobufConverter().createSolvePuzzleRequest(puzzle).toByteArray());
	}

	/**
	 * Diese Methode erzeugt ein einfaches Puzzle mit einem Symbol für 0.
	 *
	 * @return das erzeugte Puzzle
	 */
	@Nonnull
	private Puzzle generateSimplePuzzle() {
		PuzzleBuilder puzzleBuilder = new PuzzleBuilder();
		Symbol symbol0 = puzzleBuilder.findOrCreateSymbol(0, null, '0');

		puzzleBuilder.withCell(new Cell(0, 0, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 1, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 2, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(1, 0, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(1, 1, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(1, 2, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(2, 0, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(2, 1, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(2, 2, singletonList(symbol0)));
		return puzzleBuilder.build();
	}

	/**
	 * Diese Methode erzeugt ein unlösbares Puzzle.
	 *
	 * @return das erzeugte Puzzle
	 */
	@Nonnull
	private Puzzle generateUnsolvablePuzzle() {
		PuzzleBuilder puzzleBuilder = new PuzzleBuilder();
		Symbol symbol0 = puzzleBuilder.findOrCreateSymbol(0, null, 0);
		Symbol symbol1 = puzzleBuilder.findOrCreateSymbol(1, null, 0);

		puzzleBuilder.withCell(new Cell(0, 0, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 1, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 2, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(1, 0, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(1, 1, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(1, 2, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 0, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 1, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 2, singletonList(symbol1)));
		return puzzleBuilder.build();
	}

	/**
	 * Diese Methode prüft {@link SolvePuzzleCommand#execute(String[])}, wenn eine
	 * {@link IOException} fliegt.
	 */
	@Test
	public void testExecuteIOException() {
		assertThrows(
				UncheckedIOException.class,
				() -> this.objectUnderTest.execute(
						SolvePuzzleCommand.COMMAND_NAME,
						this.tempFile.getAbsolutePath() + ".unknown.file"));
	}


	/**
	 * Diese Methode prüft {@link SolvePuzzleCommand#execute(String[])}, wenn der Dateiname
	 * nicht angegeben wurde.
	 */
	@Test
	public void testExecuteMissingFileName() {
		this.objectUnderTest = new SolvePuzzleCommand();

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.execute(SolvePuzzleCommand.COMMAND_NAME));
	}
}
