package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Schnittstelle beschreibt die Fabrikmethode für {@link PuzzleSolverClient}.
 *
 * @author sirsch
 * @since 31.12.2022
 */
@FunctionalInterface
public interface PuzzleSolverClientFactory {

	/**
	 * Diese Methode erzeugt einen {@link PuzzleSolverClient}.
	 *
	 * @param host der zu verwendende Name des Hosts
	 * @param port der zu verwendende Port
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	PuzzleSolverClient create(@Nonnull String host, int port);
}
