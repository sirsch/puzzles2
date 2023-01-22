package software.sirsch.sa4e.puzzles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

/**
 * Diese Klasse stellt die Schaltstelle zum Umschalten der Log-Ausgabe bereit.
 *
 * @author sirsch
 * @since 21.01.2023
 */
public class LogOutputManager {

	/**
	 * Dieses Feld soll die Singleton-Instance dieser Klasse enthalten.
	 */
	@Nonnull
	private static final LogOutputManager SINGLETON_INSTANCE = new LogOutputManager(
			PuzzleSolverFactory.getSingletonInstance(),
			Optional.ofNullable(System.in)
					.map(InputStreamReader::new)
					.map(BufferedReader::new),
			Optional.ofNullable(System.out)
					.map(OutputStreamWriter::new)
					.map(writer -> new PrintWriter(writer, true)),
			new SolverProgressLoggerFactory(),
			solverProgressLogger ->
					puzzleSolver -> puzzleSolver.setSolverProgressLogger(solverProgressLogger),
			Thread::new);

	/**
	 * Dieses Feld enthält ein {@link CountDownLatch}, um das erste Setzen einer Log-Ausgabe
	 * abzuwarten.
	 */
	@Nonnull
	private final CountDownLatch firstSelection = new CountDownLatch(1);

	/**
	 * Dieses Feld muss die Fabrik für {@link PuzzleSolver} enthalten.
	 */
	@Nonnull
	private final PuzzleSolverFactory puzzleSolverFactory;

	/**
	 * Dieses Feld muss den Thread für die Abarbeitung der Aufträge zum Wechsel des Ausgabemediums
	 * im Hintergrund enthalten.
	 */
	@Nonnull
	private final Thread backgroundThread;

	/**
	 * Dieses Feld muss das {@link Optional} für den {@link BufferedReader} für die Konsoleneingabe
	 * enthalten.
	 */
	@Nonnull
	private final Optional<BufferedReader> consoleReader;

	/**
	 * Dieses Feld muss das {@link Optional} für den {@link PrintWriter} für die Konsolenausgabe
	 * enthalten.
	 */
	@Nonnull
	private final Optional<PrintWriter> consoleWriter;

	/**
	 * Dieses Feld muss die Fabrik für verschiedene {@link SolverProgressLogger} enthalten.
	 */
	@Nonnull
	private final SolverProgressLoggerFactory solverProgressLoggerFactory;

	/**
	 * Dieses Feld muss die Fabrikmethode für die Update-Aktion enthalten.
	 */
	@Nonnull
	private final Function<SolverProgressLogger, Consumer<PuzzleSolver>> updateActionFactory;

	/**
	 * Die Singleton-Instance wird per {@link #getSingletonInstance()} bereitgestellt.
	 *
	 * @param puzzleSolverFactory die zu setzende {@link PuzzleSolverFactory}
	 * @param consoleReader der zu setzende Reader für die Konsoleneingabe
	 * @param consoleWriter der zu setzende Writer für die Konsolenausgabe
	 * @param solverProgressLoggerFactory die zu setzende Fabrik für {@link SolverProgressLogger}
	 * @param updateActionFactory die Fabrikmethode für Update-Aktionen
	 * @param threadFactory die Fabrikmethode für {@link Thread}
	 */
	protected LogOutputManager(
			@Nonnull final PuzzleSolverFactory puzzleSolverFactory,
			@Nonnull final Optional<BufferedReader> consoleReader,
			@Nonnull final Optional<PrintWriter> consoleWriter,
			@Nonnull final SolverProgressLoggerFactory solverProgressLoggerFactory,
			@Nonnull
			final Function<SolverProgressLogger, Consumer<PuzzleSolver>> updateActionFactory,
			@Nonnull final Function<Runnable, Thread> threadFactory) {

		this.puzzleSolverFactory = puzzleSolverFactory;
		this.consoleReader = consoleReader;
		this.consoleWriter = consoleWriter;
		this.solverProgressLoggerFactory = solverProgressLoggerFactory;
		this.updateActionFactory = updateActionFactory;
		this.backgroundThread = threadFactory.apply(this::showPromptRepeatedly);
		this.backgroundThread.setDaemon(true);
	}

	/**
	 * Diese Methode gibt die Singleton-Instance dieser Klasse zurück.
	 *
	 * @return die Instanz
	 */
	@Nonnull
	public static LogOutputManager getSingletonInstance() {
		return SINGLETON_INSTANCE;
	}

	/**
	 * Diese Methode startet den Hintergrund-Thread, falls die für den Prompt benötigten Kanäle
	 * verfügbar sind.
	 */
	public void init() {
		if (this.consoleReader.isPresent() && this.consoleWriter.isPresent()) {
			this.backgroundThread.start();
		}
	}

	/**
	 * Diese Methode wartet, bis die erste Ausgabe gewählt wurde.
	 *
	 * @throws InterruptedException falls der Thread beim Warten unterbrochen wurde
	 */
	public void awaitFirstSelection() throws InterruptedException {
		this.firstSelection.await();
	}

	/**
	 * Diese Methode führt die Abfrage der Einstellung wiederholt aus.
	 */
	protected void showPromptRepeatedly() {
		while (true) {
			try {
				this.showPrompt();
			} catch (EndOfInputException e) {
				break;
			}
		}
	}

	/**
	 * Diese Methode fragt die Einstellung für die Log-Ausgabe ab.
	 */
	private void showPrompt() {
		this.consoleWriter
				.ifPresent(this::writePrompt);
		this.consoleReader
				.map(this::readPrompt)
				.ifPresentOrElse(
						this::handleUserInput,
						() -> {
							throw new EndOfInputException();
						});
	}

	/**
	 * Diese Methode gibt die Abfragemeldung für die Einstellung der Log-Ausgabe in einen
	 * {@link PrintWriter}.
	 *
	 * @param printWriter der zu verwendende Writer
	 */
	private void writePrompt(@Nonnull final PrintWriter printWriter) {
		printWriter.println("Select log output [none | stdout]:");
	}

	/**
	 * Diese Methode liest eine Zeile von der Konsole.
	 *
	 * @param reader der Reader von dem gelesen werden soll
	 * @return die gelesene Zeile oder {@code null} bei End-of-Stream
	 */
	private String readPrompt(@Nonnull final BufferedReader reader) {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new EndOfInputException();
		}
	}

	/**
	 * Diese Methode behandelt die Benutzereingabe.
	 *
	 * @param input die zu behandelnde Eingabe
	 */
	private void handleUserInput(@Nonnull final String input) {
		if (StringUtils.equalsIgnoreCase(input, "none")) {
			this.selectOutput(null);
		} else if (StringUtils.equalsIgnoreCase(input, "stdout")) {
			this.selectOutput(this.solverProgressLoggerFactory.createStdoutLogger());
		} else {
			this.consoleWriter
					.ifPresent(this::writeInvalidInputMessage);
		}
	}

	/**
	 * Diese Methode schreibt die Meldung für ungültige Eingaben in einen {@link PrintWriter}.
	 *
	 * @param printWriter der zu verwendende Writer
	 */
	private void writeInvalidInputMessage(@Nonnull final PrintWriter printWriter) {
		printWriter.println("Invalid log output!");
	}

	/**
	 * Diese Methode legt die Ausgabe fest.
	 *
	 * @param solverProgressLogger der festzulegende Ausgabemechanismus oder {@code null} für keine
	 *                             Ausgabe
	 */
	private void selectOutput(@CheckForNull final SolverProgressLogger solverProgressLogger) {
		this.puzzleSolverFactory.updateAction(this.updateActionFactory.apply(solverProgressLogger));
		this.firstSelection.countDown();
	}

	/**
	 * Diese Klasse stellt eine {@link RuntimeException} bereit, die anzeigt, dass die Eingabe
	 * vollständig gelesen worden ist.
	 */
	protected static class EndOfInputException extends RuntimeException {

		/**
		 * Dieser Konstruktor nimmt die interne Initialisierung vor.
		 */
		protected EndOfInputException() {
			super("End of input!");
		}
	}
}
