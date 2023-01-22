package software.sirsch.sa4e.puzzles;

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
	public SolverProgressLogger createStdoutLogger() {
		return new PrintStreamSolverProgressLogger(System.out);
	}
}
