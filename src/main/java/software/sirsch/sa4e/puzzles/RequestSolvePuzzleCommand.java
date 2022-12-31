package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.apache.commons.collections4.Factory;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Diese Klasse stellt das Kommando zum Erzeugen und Senden eines Puzzles an einen Server zum Lösen
 * bereit.
 *
 * @author sirsch
 * @since 29.12.2022
 */
public class RequestSolvePuzzleCommand implements Command {

	/**
	 * Diese Konstante enthält den Namen des Kommandos.
	 */
	public static final String COMMAND_NAME = "request-solve-puzzle";

	/**
	 * Dieses Feld muss den {@link PrintStream} für die Ausgabe enthalten.
	 */
	@Nonnull
	private final PrintStream out;

	/**
	 * Dieses Feld muss den {@link PuzzlePrinter} enthalten.
	 */
	@Nonnull
	private final PuzzlePrinter puzzlePrinter;

	/**
	 * Dieses Feld muss die Fabrik für {@link PuzzleGenerator} enthalten.
	 */
	@Nonnull
	private final Factory<PuzzleGenerator> puzzleGeneratorFactory;

	/**
	 * Dieses Feld muss die Fabrik für {@link PuzzleSolverClient} enthalten.
	 */
	@Nonnull
	private final PuzzleSolverClientFactory puzzleSolverClientFactory;

	/**
	 * Dieser Konstruktor führt die interne Initialisierung durch.
	 */
	public RequestSolvePuzzleCommand() {
		this(System.out, new PuzzlePrinter(), PuzzleGenerator::new, PuzzleSolverClient::new);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von Objekten zum Testen.
	 *
	 * @param out der zu setzende {@link PrintStream} für die Ausgabe
	 * @param puzzlePrinter der zu setzende {@link PuzzlePrinter}
	 * @param puzzleGeneratorFactory die zu setzende Fabrik für {@link PuzzleGenerator}
	 * @param puzzleSolverClientFactory die zu setzende Fabrik für {@link PuzzleSolverClient}
	 */
	protected RequestSolvePuzzleCommand(
			@Nonnull final PrintStream out,
			@Nonnull final PuzzlePrinter puzzlePrinter,
			@Nonnull final Factory<PuzzleGenerator> puzzleGeneratorFactory,
			@Nonnull final PuzzleSolverClientFactory puzzleSolverClientFactory) {

		this.out = out;
		this.puzzlePrinter = puzzlePrinter;
		this.puzzleGeneratorFactory = puzzleGeneratorFactory;
		this.puzzleSolverClientFactory = puzzleSolverClientFactory;
	}

	/**
	 * Diese Methode führt das Kommando aus.
	 *
	 * <p>
	 *     Dabei wird als Argument der Dateiname für die Ausgabedatei erwartet.
	 * </p>
	 *
	 * @param args die Argumente
	 */
	@Override
	public void execute(@Nonnull final String... args) {
		this.solvePuzzleRemotely(
				this.extractHost(args),
				this.extractPort(args),
				this.generatePuzzle(this.extractNumberOfDigits(args)));
	}

	/**
	 * Diese Methode ermittelt den Dateinamen aus den Argumenten.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return der ermittelte Dateiname
	 */
	@Nonnull
	private String extractHost(@Nonnull final String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException(
					"usage: request-solve-puzzle <serverHost> <serverPort> <?numberOfDigits>");
		}

		return args[1];
	}

	/**
	 * Diese Methode ermittelt die Anzahl der zu erzeugenden Stellen, falls das Argument vorhanden
	 * ist.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return der ermittelte Dateiname
	 */
	@Nonnull
	private Integer extractPort(@Nonnull final String[] args) {
		if (args.length <= 2) {
			throw new IllegalArgumentException(
					"usage: request-solve-puzzle <serverHost> <serverPort> <?numberOfDigits>");
		}

		return Integer.valueOf(args[2]);
	}


	/**
	 * Diese Methode ermittelt die Anzahl der zu erzeugenden Stellen, falls das Argument vorhanden
	 * ist.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return der ermittelte Dateiname
	 */
	@CheckForNull
	private Integer extractNumberOfDigits(@Nonnull final String[] args) {
		final int numberOfDigitsParameter = 3;

		if (args.length > numberOfDigitsParameter) {
			return Integer.valueOf(args[numberOfDigitsParameter]);
		}

		return null;
	}

	/**
	 * Diese Methode erzeugt das Puzzle.
	 *
	 * @param numberOfDigits die Anzahl der zu erzeugenden Stellen oder {@code null} für Vorgabe
	 * @return das erzeugte Puzzle
	 */
	private Puzzle generatePuzzle(@CheckForNull final Integer numberOfDigits) {
		return this.puzzleGeneratorFactory.create()
				.generate(defaultIfNull(numberOfDigits, 2));
	}

	/**
	 * Diese Methode schickt ein Rätsel zur Lösung an einen Server.
	 *
	 * @param host der Hostname des Servers
	 * @param port der Port des Servers
	 * @param puzzle das zu lösende Puzzle
	 */
	private void solvePuzzleRemotely(
			@Nonnull final String host,
			@Nonnull final Integer port,
			@Nonnull final Puzzle puzzle) {

		Map<Integer, Integer> result;

		this.puzzlePrinter.print(puzzle);
		result = this.solvePuzzleWithNewClient(host, port, puzzle);
		this.out.println("Solution found:");
		this.printSymbols(puzzle.getSymbols(), result);
	}

	/**
	 * Diese Methode erzeugt einen neuen Client und sendet mit diesem die Anfrage zum Lösen eines
	 * Rätsels an den Server.
	 *
	 * @param host der Hostname des Servers
	 * @param port der Port des Servers
	 * @param puzzle das zu lösende Puzzle
	 * @return die ermittelte Antwort
	 */
	@Nonnull
	private Map<Integer, Integer> solvePuzzleWithNewClient(
			@Nonnull final String host,
			@Nonnull final Integer port,
			@Nonnull final Puzzle puzzle) {

		try (PuzzleSolverClient client = this.puzzleSolverClientFactory.create(host, port)) {
			return client.solvePuzzle(puzzle);
		}
	}

	/**
	 * Diese Methode gibt die Symbole und deren Werte aus.
	 *
	 * @param symbols die Liste der auszugebenden Symbole
	 * @param mapping die Zuordnung von Symbol-IDs zu den ermittelten Werten
	 */
	private void printSymbols(
			@Nonnull final List<Symbol> symbols,
			@Nonnull final Map<Integer, Integer> mapping) {

		symbols.forEach(symbol -> this.out.println("Symbol "
				+ Character.toString(symbol.getIconCodePoint()) + " ID: " + symbol.getId()
				+ ", digit value: " + mapping.get(symbol.getId())));
	}
}
