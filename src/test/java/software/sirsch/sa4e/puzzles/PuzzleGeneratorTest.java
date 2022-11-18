package software.sirsch.sa4e.puzzles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse stellt Tests für {@link PuzzleGenerator} bereit.
 *
 * @author sirsch
 * @since 18.11.2022
 */
public class PuzzleGeneratorTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleGenerator objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new PuzzleGenerator();
	}

	/**
	 * Diese Methode prüft {@link PuzzleGenerator#generate()}.
	 */
	@Test
	public void testGenerate() {
		this.objectUnderTest.generate();
	}
}