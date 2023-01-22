package software.sirsch.sa4e.puzzles;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt eine Fabrik für verschiedene {@link SolverProgressLogger}s bereit.
 *
 * @author sirsch
 * @since 22.01.2023
 */
public class SolverProgressLoggerFactory {

	/**
	 * Diese Methode gibt einen {@link SolverProgressLogger} für die Ausgabe nach {@link System#out}
	 * zurück.
	 *
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	public SolverProgressLogger createStdoutLogger() {
		return new PrintStreamSolverProgressLogger(System.out);
	}

	/**
	 * Diese Methode erzeugt einen {@link SolverProgressLogger} für die Ausgabe in eine Datei.
	 *
	 * @param filename der Name der zu schreibenden Datei
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	public SolverProgressLogger createFileLogger(@Nonnull final String filename) {
		try {
			return new PrintStreamSolverProgressLogger(new PrintStream(
					new FileOutputStream(filename),
					true,
					StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			throw new UncheckedIOException(e);
		}
	}
}
