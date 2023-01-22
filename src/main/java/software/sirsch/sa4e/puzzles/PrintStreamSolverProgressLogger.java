package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;

import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt einen {@link SolverProgressLogger} bereit, der in ein {@link PrintStream}
 * loggt.
 *
 * @author sirsch
 * @since 20.01.2023
 */
public class PrintStreamSolverProgressLogger implements SolverProgressLogger {

	/**
	 * Dieses Feld muss den {@link PrintStream} für die Ausgabe enthalten.
	 */
	@Nonnull
	private final PrintStream out;

	/**
	 * Dieser Initialisierungskonstruktor legt den {@link PrintStream} für die Ausgabe fest.
	 *
	 * @param out der zu setzende Ausgabedatenstrom
	 */
	public PrintStreamSolverProgressLogger(@Nonnull final PrintStream out) {
		this.out = out;
	}

	@Override
	public void log(@Nonnull final String message) {
		this.out.println(message);
	}

	@Override
	public void close() {
		this.out.close();
	}
}
