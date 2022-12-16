package software.sirsch.sa4e.puzzles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse stellt Tests für {@link HelpCommand} bereit.
 *
 * @author sirsch
 * @since 16.12.2022
 */
public class HelpCommandTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private HelpCommand objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new HelpCommand();
	}

	/**
	 * Diese Methode prüft {@link HelpCommand#execute(String[])}.
	 */
	@Test
	public void testExecute() {
		this.objectUnderTest.execute(new String[0]);

		/* no exception thrown */
	}
}
