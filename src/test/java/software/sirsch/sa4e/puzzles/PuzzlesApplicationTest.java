package software.sirsch.sa4e.puzzles;

import java.util.Map;

import org.apache.commons.collections4.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Diese Klasse stellt Tests für {@link PuzzlesApplication} bereit.
 *
 * @author sirsch
 * @since 16.12.2022
 */
public class PuzzlesApplicationTest {

	/**
	 * Dieses Feld soll den Mock für {@link Command} enthalten.
	 */
	private Command command;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzlesApplication objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.command = mock(Command.class);

		this.objectUnderTest = new PuzzlesApplication(Map.of(
				"test-command", () -> this.command,
				"other-command", mock(Factory.class)));
	}

	/**
	 * Diese Methode prüft {@link PuzzlesApplication#main(String[])}.
	 */
	@Test
	public void testMain() {
		PuzzlesApplication.main(new String[0]);
	}

	/**
	 * Diese Methode prüft {@link PuzzlesApplication#executeCommand(String[])}.
	 */
	@Test
	public void testExecuteCommand() {
		this.objectUnderTest.executeCommand(new String[] {"test-command", "test", "arguments"});

		verify(this.command).execute(new String[] {"test-command", "test", "arguments"});
	}
}