package software.sirsch.sa4e.puzzles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link LogOutputManager} bereit.
 *
 * @author sirsch
 * @since 22.01.2023
 */
public class LogOutputManagerTest {

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleSolverFactory} enthalten.
	 */
	private PuzzleSolverFactory puzzleSolverFactory;

	/**
	 * Dieses Feld soll den Mock für den {@link BufferedReader} zum Lesen der Konsoleneingabe
	 * enthalten.
	 */
	private BufferedReader consoleReader;

	/**
	 * Dieses Feld soll den Mock für den {@link PrintWriter} zum Schreiben der Konsolenausgaben
	 * enthalten.
	 */
	private PrintWriter consoleWriter;

	/**
	 * Dieses Feld soll den Mock für {@link SolverProgressLoggerFactory} enthalten.
	 */
	private SolverProgressLoggerFactory solverProgressLoggerFactory;

	/**
	 * Dieses Feld soll den Mock für die Fabrikmethode für die Update-Aktion enthalten.
	 */
	private Function<SolverProgressLogger, Consumer<PuzzleSolver>> updateActionFactory;

	/**
	 * Dieses Feld soll den Mock für den Hintergrund-Thread enthalten.
	 */
	private Thread backgroundThread;

	/**
	 * Dieses Feld soll den Mock für die Fabrikmethode für den Hintergrund-Thread enthalten.
	 */
	private Function<Runnable, Thread> threadFactory;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private LogOutputManager objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.puzzleSolverFactory = mock(PuzzleSolverFactory.class);
		this.consoleReader = mock(BufferedReader.class);
		this.consoleWriter = mock(PrintWriter.class);
		this.threadFactory = mock(Function.class);
		this.solverProgressLoggerFactory = mock(SolverProgressLoggerFactory.class);
		this.updateActionFactory = mock(Function.class);
		this.backgroundThread = mock(Thread.class);
		when(this.threadFactory.apply(notNull())).thenReturn(this.backgroundThread);

		this.objectUnderTest = new LogOutputManager(
				this.puzzleSolverFactory,
				Optional.of(this.consoleReader),
				Optional.of(this.consoleWriter),
				this.solverProgressLoggerFactory,
				this.updateActionFactory,
				this.threadFactory);
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#LogOutputManager(PuzzleSolverFactory, Optional,
	 * Optional, SolverProgressLoggerFactory, Function, Function)}.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testConstructor() throws IOException {
		ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);

		when(this.consoleReader.readLine()).thenReturn(null);

		/* Das #objectUnderTest wird in Methode #setUp erzeugt. */

		verify(this.threadFactory).apply(runnableArgumentCaptor.capture());
		assertNotNull(runnableArgumentCaptor.getValue());
		verifyNoInteractions(this.consoleReader);
		verifyNoInteractions(this.consoleWriter);
		runnableArgumentCaptor.getValue().run();
		verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#getSingletonInstance()}.
	 */
	@Test
	public void testGetSingletonInstance() {
		assertSame(
				LogOutputManager.getSingletonInstance(),
				LogOutputManager.getSingletonInstance());
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#init()}.
	 */
	@Test
	public void testInit() {
		this.objectUnderTest.init();

		verify(this.backgroundThread).start();
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#awaitFirstSelection()}.
	 */
	@Test
	public void testAwaitFirstSelection() throws IOException {
		ScheduledFuture<?> future = Executors.newSingleThreadScheduledExecutor().schedule(
				() -> {
					try {
						this.objectUnderTest.awaitFirstSelection();
					} catch (InterruptedException e) {
						fail(e);
					}
				},
				0L,
				TimeUnit.SECONDS);

		when(this.consoleReader.readLine()).thenReturn("stdout", null);
		assertThrows(TimeoutException.class, () -> future.get(3, TimeUnit.SECONDS));

		this.objectUnderTest.showPromptRepeatedly();

		assertDoesNotThrow(() -> future.get(3, TimeUnit.SECONDS));
		assertDoesNotThrow(this.objectUnderTest::awaitFirstSelection);
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#init()} wenn der Reader fehlt.
	 */
	@Test
	public void testInitReaderNotPresent() {
		this.objectUnderTest = new LogOutputManager(
				this.puzzleSolverFactory,
				Optional.of(this.consoleReader),
				Optional.empty(),
				this.solverProgressLoggerFactory,
				this.updateActionFactory,
				this.threadFactory);

		this.objectUnderTest.init();

		verify(this.backgroundThread, never()).start();
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#init()} wenn der Writer fehlt.
	 */
	@Test
	public void testInitWriterNotPresent() {
		this.objectUnderTest = new LogOutputManager(
				this.puzzleSolverFactory,
				Optional.empty(),
				Optional.of(this.consoleWriter),
				this.solverProgressLoggerFactory,
				this.updateActionFactory,
				this.threadFactory);

		this.objectUnderTest.init();

		verify(this.backgroundThread, never()).start();
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#showPromptRepeatedly()}.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testShowPromptRepeatedly() throws IOException {
		InOrder orderVerifier = inOrder(
				this.consoleReader,
				this.consoleWriter,
				this.puzzleSolverFactory);
		SolverProgressLogger stdoutLogger = mock(SolverProgressLogger.class);
		Consumer<PuzzleSolver> noneUpdateAction = mock(Consumer.class);
		Consumer<PuzzleSolver> stdoutUpdateAction = mock(Consumer.class);

		when(this.consoleReader.readLine()).thenReturn("none", "stdout", null);
		when(this.solverProgressLoggerFactory.createStdoutLogger()).thenReturn(stdoutLogger);
		when(this.updateActionFactory.apply(null)).thenReturn(noneUpdateAction);
		when(this.updateActionFactory.apply(stdoutLogger)).thenReturn(stdoutUpdateAction);

		this.objectUnderTest.showPromptRepeatedly();

		orderVerifier.verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
		orderVerifier.verify(this.consoleReader).readLine();
		orderVerifier.verify(this.puzzleSolverFactory).updateAction(noneUpdateAction);
		orderVerifier.verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
		orderVerifier.verify(this.consoleReader).readLine();
		orderVerifier.verify(this.puzzleSolverFactory).updateAction(stdoutUpdateAction);
		orderVerifier.verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
		orderVerifier.verify(this.consoleReader).readLine();
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#showPromptRepeatedly()} mit ungültiger Eingabe.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testShowPromptRepeatedlyInvalidInput() throws IOException {
		InOrder orderVerifier = inOrder(
				this.consoleReader,
				this.consoleWriter,
				this.puzzleSolverFactory);
		SolverProgressLogger stdoutLogger = mock(SolverProgressLogger.class);
		Consumer<PuzzleSolver> stdoutUpdateAction = mock(Consumer.class);

		when(this.consoleReader.readLine()).thenReturn("invalid", "stdout", null);
		when(this.solverProgressLoggerFactory.createStdoutLogger()).thenReturn(stdoutLogger);
		when(this.updateActionFactory.apply(stdoutLogger)).thenReturn(stdoutUpdateAction);

		this.objectUnderTest.showPromptRepeatedly();

		orderVerifier.verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
		orderVerifier.verify(this.consoleReader).readLine();
		orderVerifier.verify(this.consoleWriter).println("Invalid log output!");
		orderVerifier.verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
		orderVerifier.verify(this.consoleReader).readLine();
		orderVerifier.verify(this.puzzleSolverFactory).updateAction(stdoutUpdateAction);
		orderVerifier.verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
		orderVerifier.verify(this.consoleReader).readLine();
	}

	/**
	 * Diese Methode prüft {@link LogOutputManager#showPromptRepeatedly()} wenn eine
	 * {@link IOException} auftritt.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testShowPromptRepeatedlyIOException() throws IOException {
		InOrder orderVerifier = inOrder(
				this.consoleReader,
				this.consoleWriter,
				this.puzzleSolverFactory);

		when(this.consoleReader.readLine()).thenThrow(new IOException("testException"));

		assertDoesNotThrow(() -> this.objectUnderTest.showPromptRepeatedly());

		orderVerifier.verify(this.consoleWriter).println(argThatStartsWith("Select log output"));
		orderVerifier.verify(this.consoleReader).readLine();
	}

	/**
	 * Diese Methode erzeugt einen Argument-Matcher der einen String auf ein Präfix vergleicht.
	 *
	 * @param prefix das erwartete Präfix
	 * @return der erzeugte Matcher
	 */
	@Nonnull
	private static String argThatStartsWith(@Nonnull final String prefix) {
		return argThat(string -> StringUtils.startsWith(string, prefix));
	}
}
