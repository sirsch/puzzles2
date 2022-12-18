package software.sirsch.sa4e.puzzles;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Diese Klasse stellt Tests für {@link GeneratePuzzleCommand} bereit.
 *
 * @author sirsch
 * @since 15.12.2022
 */
public class GeneratePuzzleCommandTest {

	/**
	 * Dieses Feld muss die Datei zum Testen enthalten.
	 */
	private File tempFile;

	/**
	 * Dieses Feld soll den Mock für den {@link PuzzlePrinter} enthalten.
	 */
	private PuzzlePrinter puzzlePrinter;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private GeneratePuzzleCommand objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() throws IOException {
		this.tempFile = File.createTempFile("tempFile", ".test");
		this.puzzlePrinter = mock(PuzzlePrinter.class);

		this.objectUnderTest = new GeneratePuzzleCommand(this.puzzlePrinter);
	}

	/**
	 * Diese Methode räumt die Testumgebung nach jedem Testfall auf.
	 */
	@AfterEach
	public void tearDown() {
		assertTrue(this.tempFile.delete(), "Die Temp-Datei konnte nicht gelöscht werden!");
	}

	/**
	 * Diese Methode prüft {@link GeneratePuzzleCommand#execute(String[])}.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testExecute() throws IOException {
		this.objectUnderTest.execute(
				GeneratePuzzleCommand.COMMAND_NAME,
				this.tempFile.getAbsolutePath(),
				"6");

		this.verifyResult();
		verify(this.puzzlePrinter).print(notNull());
	}

	/**
	 * Diese Methode prüft {@link GeneratePuzzleCommand#execute(String[])}.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testExecuteNoNumberOfDigits() throws IOException {
		this.objectUnderTest.execute(
				GeneratePuzzleCommand.COMMAND_NAME,
				this.tempFile.getAbsolutePath());

		this.verifyResult();
		verify(this.puzzlePrinter).print(notNull());
	}

	/**
	 * Diese Methode prüft das in die Datei geschriebene Puzzle.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	private void verifyResult() throws IOException {
		Puzzles.SolvePuzzleRequest puzzle = Puzzles.SolvePuzzleRequest.parseFrom(
				FileUtils.readFileToByteArray(this.tempFile));

		assertTrue(puzzle.getSymbolsCount() > 0);
		assertEquals(9, puzzle.getCellsCount());
	}

	/**
	 * Diese Methode prüft {@link GeneratePuzzleCommand#execute(String[])}, wenn eine
	 * {@link IOException} fliegt.
	 */
	@Test
	public void testExecuteIOException() {
		assertTrue(this.tempFile.delete());
		assertTrue(this.tempFile.mkdir());

		assertThrows(
				UncheckedIOException.class,
				() -> this.objectUnderTest.execute(
						GeneratePuzzleCommand.COMMAND_NAME,
						this.tempFile.getAbsolutePath(),
						"6"));
	}

	/**
	 * Diese Methode prüft {@link GeneratePuzzleCommand#execute(String[])}, wenn der Dateiname
	 * nicht angegeben wurde.
	 */
	@Test
	public void testExecuteMissingFileName() {
		this.objectUnderTest = new GeneratePuzzleCommand();

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.execute(GeneratePuzzleCommand.COMMAND_NAME));
	}
}
