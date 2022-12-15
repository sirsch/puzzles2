package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Methode beschreibt die Methode zum Ausführen eines Kommandos.
 *
 * @author sirsch
 * @since 15.12.2022
 */
public interface Command {

	/**
	 * Diese Methode führt das Kommando aus.
	 *
	 * @param args die Argumente
	 */
	void execute(@Nonnull String[] args);
}
