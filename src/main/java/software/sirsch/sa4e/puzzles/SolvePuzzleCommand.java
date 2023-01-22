package software.sirsch.sa4e.puzzles;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.List;

import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

/**
 * Diese Klasse stellt das Kommando zum Erzeugen eines Puzzles bereit.
 *
 * @author sirsch
 * @since 17.12.2022
 */
public class SolvePuzzleCommand implements Command {

	/**
	 * Diese Konstante enthält den Namen des Kommandos.
	 */
	public static final String COMMAND_NAME = "solve-puzzle";

	/**
	 * Dieses Feld muss den für die Ausgabe zu verwendenden {@link PrintStream} enthalten.
	 */
	@Nonnull
	private final PrintStream out;

	/**
	 * Dieses Feld muss den {@link PuzzlePrinter} enthalten.
	 */
	@Nonnull
	private final PuzzlePrinter puzzlePrinter;

	/**
	 * Dieses Feld muss die Fabrik für {@link PuzzleSolver} enthalten.
	 */
	@Nonnull
	private final PuzzleSolverFactory puzzleSolverFactory;

	/**
	 * Dieses Feld muss den {@link LogOutputManager} enthalten.
	 */
	@Nonnull
	private final LogOutputManager logOutputManager;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public SolvePuzzleCommand() {
		this(
				System.out,
				new PuzzlePrinter(),
				PuzzleSolverFactory.getSingletonInstance(),
				LogOutputManager.getSingletonInstance());
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen des {@link PrintStream} out und des
	 * {@link PuzzlePrinter} zum Testen.
	 *
	 * @param out der zu setzende {@link PrintStream}
	 * @param puzzlePrinter der zu setzende {@link PuzzlePrinter}
	 * @param puzzleSolverFactory die zu setzende {@link PuzzleSolverFactory}
	 * @param logOutputManager die zu setzende {@link LogOutputManager}
	 */
	protected SolvePuzzleCommand(
			@Nonnull final PrintStream out,
			@Nonnull final PuzzlePrinter puzzlePrinter,
			@Nonnull final PuzzleSolverFactory puzzleSolverFactory,
			@Nonnull final LogOutputManager logOutputManager) {

		this.out = out;
		this.puzzlePrinter = puzzlePrinter;
		this.puzzleSolverFactory = puzzleSolverFactory;
		this.logOutputManager = logOutputManager;
	}

	/**
	 * Diese Methode führt das Kommando aus.
	 *
	 * <p>
	 *     Dabei wird als Argument der Dateiname für die Eingabedatei erwartet.
	 * </p>
	 *
	 * @param args die Argumente
	 */
	@Override
	public void execute(@Nonnull final String... args) {
		this.solvePuzzle(this.extractFilename(args), this.extractDelay(args));
	}

	/**
	 * Diese Methode ermittelt den Dateinamen aus den Argumenten.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return der ermittelte Dateiname
	 */
	@Nonnull
	private String extractFilename(@Nonnull final String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("usage: solve-puzzle <filename>");
		}

		return args[1];
	}

	/**
	 * Diese Methode ermittelt das Delay in Millisekunden.
	 *
	 * @param args die zu übergebenden Argumente
	 * @return das ermittelte Delay in Millisekunden
	 */
	private int extractDelay(@Nonnull final String[] args) {
		if (args.length > 2) {
			return Integer.valueOf(args[2]);
		} else {
			return 0;
		}
	}

	/**
	 * Diese Methode löst ein Puzzle.
	 *
	 * @param filename der Name der Eingabedatei
	 * @param delay das Delay in Millisekunden
	 */
	private void solvePuzzle(@Nonnull final String filename, final int delay) {
		try (InputStream inputStream = new FileInputStream(filename)) {
			this.solvePuzzle(
					new Protobuf2PuzzleConverter().createPuzzle(
							Puzzles.SolvePuzzleRequest.parseFrom(inputStream)),
					delay);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Diese Methode löst ein Rätsel und gibt das Ergebnis aus.
	 *
	 * @param puzzle das zu lösende Puzzle
	 * @param delay das Delay in Millisekunden
	 */
	private void solvePuzzle(@Nonnull final Puzzle puzzle, final int delay) {
		this.puzzlePrinter.print(puzzle);
		this.out.println();
		this.logOutputManager.init();

		try {
			this.logOutputManager.awaitFirstSelection();
		} catch (InterruptedException e) {
			return;
		}

		this.printSolution(this.puzzleSolverFactory.create(delay).solvePuzzle(puzzle));
	}

	/**
	 * Diese Methode gibt das Ergebnis des Lösungsversuchs aus.
	 *
	 * <p>
	 *     Eine leere Liste bedeutet, dass das Rätsel keine Lösung hat.
	 * </p>
	 *
	 * @param solution das Ergebnis
	 */
	private void printSolution(@Nonnull final List<Symbol> solution) {
		if (solution.isEmpty()) {
			this.out.println("No solution found!");
		} else {
			this.out.println("Solution found:");
			this.printSymbols(solution);
		}
	}

	/**
	 * Diese Methode gibt die Symbole und deren Werte aus.
	 *
	 * @param symbols die Liste der auszugebenden Symbole
	 */
	private void printSymbols(@Nonnull final List<Symbol> symbols) {
		symbols.forEach(symbol -> this.out.println("Symbol "
				+ Character.toString(symbol.getIconCodePoint()) + " ID: " + symbol.getId()
				+ ", digit value: " + symbol.getBoundValue()));
	}
}
