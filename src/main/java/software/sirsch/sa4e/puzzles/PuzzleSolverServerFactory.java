package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Schnittstelle beschreibt die Fabrikmethode für {@link PuzzleSolverServer}.
 */
@FunctionalInterface
public interface PuzzleSolverServerFactory {

	/**
	 * Diese Methode erzeugt einen {@link PuzzleSolverServer} und legt dabei den Port fest, auf dem
	 * der Server Verbindungen entgegennimmt.
	 *
	 * @param port der zu verwendende Port
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	PuzzleSolverServer create(int port);
}
