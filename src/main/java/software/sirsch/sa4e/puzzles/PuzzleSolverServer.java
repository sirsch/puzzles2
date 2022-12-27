package software.sirsch.sa4e.puzzles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Diese Klasse stellt den gRPC-Server für {@link PuzzleSolverService} bereit.
 *
 * @author sirsch
 * @since 27.12.2022
 */
public class PuzzleSolverServer {

	/**
	 * Dieses Feld muss den {@link Server} enthalten.
	 */
	@Nonnull
	private final Server server;

	/**
	 * Dieses Feld muss dem {@link Consumer} zum Hinzufügen von Shutdown-Hooks enthalten.
	 *
	 * <p>
	 *     Im Produktiven Einsatz wird dazu {@link Runtime#addShutdownHook(Thread)} verwendet. Zum
	 *     Testen kann aber ein einfacher Mock verwendet werden.
	 * </p>
	 */
	@Nonnull
	private final Consumer<Thread> shutdownHookAdder;

	/**
	 * Dieser Konstruktor legt den Port fest, auf dem der Server Verbindungen entgegennimmt.
	 *
	 * @param port der zu verwendende Port
	 */
	public PuzzleSolverServer(final int port) {
		this(
				ServerBuilder.forPort(port).addService(new PuzzleSolverService()).build(),
				Runtime.getRuntime()::addShutdownHook);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von Objekten zum Testen.
	 *
	 * @param server der zu setzende Server
	 * @param shutdownHookAdder der zu setzende {@link Consumer} zum Hinzufügen von Shutdown-Hooks
	 */
	protected PuzzleSolverServer(
			@Nonnull final Server server,
			@Nonnull final Consumer<Thread> shutdownHookAdder) {

		this.server = server;
		this.shutdownHookAdder = shutdownHookAdder;
	}

	/**
	 * Diese Methode führt den Server aus.
	 *
	 * <p>
	 *     Dabei startet die Methode den Server und wartet das Beenden des Servers ab.
	 * </p>
	 */
	public void run() {
		this.registerShutdownHook();
		this.start();
		this.awaitTermination();
	}

	/**
	 * Diese Methode registriert einen Shutdown-Hook für {@link #stop()}.
	 */
	protected void registerShutdownHook() {
		this.shutdownHookAdder.accept(new Thread(this::stop));
	}

	/**
	 * Diese Methode startet den Server.
	 */
	protected void start() {
		try {
			this.server.start();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Diese Methode stoppt den Server.
	 */
	protected void stop() {
		try {
			this.server.shutdown().awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Diese Methode wartet das Stoppen des Servers ab.
	 */
	protected void awaitTermination() {
		try {
			this.server.awaitTermination();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
