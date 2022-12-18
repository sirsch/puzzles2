package software.sirsch.sa4e.puzzles;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Diese Klasse stellt das Kommando zum Erzeugen eines Puzzles bereit.
 *
 * @author sirsch
 * @since 15.12.2022
 */
public class GeneratePuzzleCommand implements Command {

	/**
	 * Diese Konstante enthält den Namen des Kommandos.
	 */
	public static final String COMMAND_NAME = "generate-puzzle";

	/**
	 * Dieses Feld muss den {@link PuzzlePrinter} enthalten.
	 */
	@Nonnull
	private final PuzzlePrinter puzzlePrinter;

	/**
	 * Dieser Konstruktor führt die interne Initialisierung durch.
	 */
	public GeneratePuzzleCommand() {
		this(new PuzzlePrinter());
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von {@link PuzzlePrinter} zum Testen.
	 *
	 * @param puzzlePrinter der zu setzende {@link PuzzlePrinter}
	 */
	protected GeneratePuzzleCommand(@Nonnull final PuzzlePrinter puzzlePrinter) {
		this.puzzlePrinter = puzzlePrinter;
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
		this.generatePuzzle(this.extractFilename(args), this.extractNumberOfDigits(args));
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
			throw new IllegalArgumentException(
					"usage: generate-puzzle <filename> <?numberOfDigits>");
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
	@CheckForNull
	private Integer extractNumberOfDigits(@Nonnull final String[] args) {
		if (args.length > 2) {
			return Integer.valueOf(args[2]);
		}

		return null;
	}

	/**
	 * Diese Methode erzeugt das Puzzle.
	 *
	 * @param filename der Name der Ausgabedatei
	 * @param numberOfDigits die Anzahl der zu erzeugenden Stellen oder {@code null} für Vorgabe
	 */
	private void generatePuzzle(
			@Nonnull final String filename,
			@CheckForNull final Integer numberOfDigits) {

		try (OutputStream outputStream = new FileOutputStream(filename)) {
			this.writePuzzle(
					new PuzzleGenerator().generate(defaultIfNull(numberOfDigits, 2)),
					outputStream);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Diese Methode schreibt ein Puzzle in die Ausgabe.
	 *
	 * @param puzzle das zu schreibende Puzzle
	 * @param outputStream die zu beschreibende Ausgabe
	 * @throws IOException zeigt einen Datenübertragungsfehler an
	 */
	private void writePuzzle(
			@Nonnull final Puzzle puzzle,
			@Nonnull final OutputStream outputStream) throws IOException {

		this.puzzlePrinter.print(puzzle);
		new Puzzle2ProtobufConverter().createSolvePuzzleRequest(puzzle).writeTo(outputStream);
	}
}
