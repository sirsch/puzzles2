package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt den Generator für Zahlenrätsel bereit.
 *
 * @author sirsch
 * @since 18.11.2022
 */
public class PuzzleGenerator {

	/**
	 * Diese Methode behandelt die Ausführung des Generators von der Kommandozeile aus.
	 *
	 * @param args die zu verarbeitenden Argumente
	 */
	public static void main(@Nonnull final String[] args) {
		new PuzzleGenerator().generate();
	}

	/**
	 * Diese Methode erzeugt ein neues Zahlenrätsel.
	 */
	public void generate() {
		System.out.println("Hello world!");
	}
}
