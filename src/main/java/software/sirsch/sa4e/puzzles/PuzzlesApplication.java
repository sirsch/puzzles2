package software.sirsch.sa4e.puzzles;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.Factory;

/**
 * Diese Klasse enthält die Main-Methode der Anwendung.
 *
 * @author sirsch
 * @since 10.12.2022
 */
public class PuzzlesApplication {

	/**
	 * Diese Konstante enthält die Singleton-Instanz dieser Klasse.
	 */
	private static final PuzzlesApplication DEFAULT_INSTANCE = new PuzzlesApplication(Map.of(
			GeneratePuzzleCommand.COMMAND_NAME, GeneratePuzzleCommand::new,
			SolvePuzzleCommand.COMMAND_NAME, SolvePuzzleCommand::new,
			RunServerCommand.COMMAND_NAME, RunServerCommand::new,
			HelpCommand.COMMAND_NAME, HelpCommand::new));

	/**
	 * Dieses Feld muss die Zuordnung von Kommandonamen zu einer Fabrik für das Kommando enthalten.
	 */
	@Nonnull
	private final Map<String, Factory<Command>> commands;

	/**
	 * Dieser Konstruktor legt die verfügbaren {@link Command}s fest.
	 *
	 * @param commands die Zuordnung von Befehlen und deren Namen
	 */
	protected PuzzlesApplication(@Nonnull final Map<String, Factory<Command>> commands) {
		this.commands = commands;
	}

	/**
	 * Diese Methode startet die Anwendung.
	 *
	 * @param args die zu verwendenden Argumente
	 */
	public static void main(@Nonnull final String[] args) {
		DEFAULT_INSTANCE.executeCommand(args);
	}

	/**
	 * Diese Methode startet die Anwendung.
	 *
	 * @param args die zu verwendenden Argumente
	 */
	public void executeCommand(@Nonnull final String[] args) {
		this.extractCommandName(args)
				.map(this.commands::get)
				.orElse(HelpCommand::new)
				.create()
				.execute(args);
	}

	/**
	 * Diese Methode ermittelt den Namen des Kommandos.
	 *
	 * @param args die auszuwertenden Argumente
	 * @return der optional ermittelte Name
	 */
	@Nonnull
	private Optional<String> extractCommandName(@Nonnull final String[] args) {
		if (args.length == 0) {
			return Optional.empty();
		}

		return Optional.ofNullable(args[0]);
	}
}
