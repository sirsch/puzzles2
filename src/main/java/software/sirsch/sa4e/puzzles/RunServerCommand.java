package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;

import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt das Kommando zum Betreiben des Servers bereit.
 *
 * @author sirsch
 * @since 29.12.2022
 */
public class RunServerCommand implements Command {

	/**
	 * Diese Konstante enthält den Namen des Kommandos.
	 */
	public static final String COMMAND_NAME = "run-server";

	/**
	 * Dieses Feld muss die Fabrik für {@link PuzzleSolverServer} enthalten.
	 */
	@Nonnull
	private final PuzzleSolverServerFactory puzzleSolverServerFactory;

	/**
	 * Dieses Feld muss den {@link PrintStream} für die Ausgabe enthalten.
	 */
	@Nonnull
	private final PrintStream out;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public RunServerCommand() {
		this(PuzzleSolverServer::new, System.out);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von Objekten zum Testen.
	 *
	 * @param puzzleSolverServerFactory die zu setzende Fabrik für {@link PuzzleSolverServer}
	 * @param out der zu setzende {@link PrintStream} für die Ausgabe
	 */
	protected RunServerCommand(
			@Nonnull final PuzzleSolverServerFactory puzzleSolverServerFactory,
			@Nonnull final PrintStream out) {

		this.puzzleSolverServerFactory = puzzleSolverServerFactory;
		this.out = out;
	}

	/**
	 * Diese Methode führt das Kommando aus.
	 *
	 * <p>
	 *     Dabei wird als Argument der Dateiname für die Ausgabedatei erwartet.
	 * </p>
	 *
	 * @param args die Argumente
	 */
	@Override
	public void execute(@Nonnull final String... args) {
		this.runServer(this.extractPort(args));
	}

	/**
	 * Diese Methode ermittelt die Port-Nummer.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return der ermittelte Port
	 */
	@Nonnull
	private Integer extractPort(@Nonnull final String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("usage: run-server <port>");
		}

		return Integer.valueOf(args[1]);
	}

	/**
	 * Diese Methode erzeugt und startet einen {@link PuzzleSolverServer}.
	 *
	 * @param port die zu verwendende Portnummer
	 */
	private void runServer(@Nonnull final Integer port) {
		this.out.println("Starting server...");
		this.puzzleSolverServerFactory.create(port).run();
		this.out.println("Server stopped!");
	}
}
