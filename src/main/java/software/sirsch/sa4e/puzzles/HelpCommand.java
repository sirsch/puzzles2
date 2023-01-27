package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt das Kommando bereit, das die Hilfe ausgibt.
 *
 * @author sirsch
 * @since 16.12.2022
 */
public class HelpCommand implements Command {

	/**
	 * Diese Konstante enthält den Namen des Kommandos.
	 */
	public static final String COMMAND_NAME = "help";

	@Override
	public void execute(@Nonnull final String[] args) {
		System.out.println("usage: generate-puzzle <filename> <?numberOfDigits>");
		System.out.println("usage: solve-puzzle <filename>");
		System.out.println("usage: run-server <port>");
		System.out.println(
				"usage: request-solve-puzzle <serverHost> <serverPort> <?numberOfDigits>");
		System.out.println("usage: "
				+ "run-camel <mqttBrokerUrl> <grpcServer> <?generatePuzzleNumberOfDigits>");
	}
}
