package software.sirsch.sa4e.puzzles;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	 * Dieses Feld soll eine Temporärdatei zum Testen enthalten.
	 */
	private File tempFile;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private SolverProgressLoggerFactory objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@BeforeEach
	public void setUp() throws IOException {
		this.tempFile = File.createTempFile("test.", ".log");

		this.objectUnderTest = new SolverProgressLoggerFactory();
	}

	/**
	 * Diese Methode räumt die Testumgebung nach jedem Testfall auf.
	 */
	@AfterEach
	public void tearDown() {
		assertTrue(this.tempFile.delete(), "Die Temp-Datei konnte nicht gelöscht werden!");
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

	/**
	 * Diese Methode prüft {@link SolverProgressLoggerFactory#createFileLogger(String)}.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testCreateFileLogger() throws IOException {
		SolverProgressLogger result;

		result = this.objectUnderTest.createFileLogger(this.tempFile.getAbsolutePath());
		result.log("testMessage");
		result.close();

		assertEquals(
				"testMessage" + System.lineSeparator(),
				FileUtils.readFileToString(this.tempFile, StandardCharsets.UTF_8));
	}

	/**
	 * Diese Methode prüft {@link SolverProgressLoggerFactory#createFileLogger(String)}, wenn eine
	 * {@link IOException} fliegt.
	 */
	@Test
	public void testCreateFileLoggerIOException() {
		assertTrue(this.tempFile.delete());
		assertTrue(this.tempFile.mkdir());

		assertThrows(
				UncheckedIOException.class,
				() -> this.objectUnderTest.createFileLogger(this.tempFile.getAbsolutePath()));
	}

	/**
	 * Diese Methode prüft {@link SolverProgressLoggerFactory#createMqttLogger(String)}.
	 */
	@Test
	public void testCreateMqttLogger() {
		SolverProgressLogger result;

		result = this.objectUnderTest.createMqttLogger("tcp://dummy:1883");

		assertNotNull(result);
	}
}
