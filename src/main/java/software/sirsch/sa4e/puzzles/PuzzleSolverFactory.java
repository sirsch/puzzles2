package software.sirsch.sa4e.puzzles;

import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static java.util.Collections.newSetFromMap;

/**
 * Diese Klasse stellt eine Fabrik für {@link PuzzleSolver} bereit.
 *
 * <p>
 *     Dabei verwaltet diese Klasse eine Aktion, die bei der Erzeugung von {@link PuzzleSolver}s
 *     oder bei der Änderung der Aktion angewendet wird.
 * </p>
 *
 * @author sirsch
 * @since 20.01.2023
 */
public class PuzzleSolverFactory {

	/**
	 * Dieses Feld soll die Singleton-Instance dieser Klasse enthalten.
	 */
	@Nonnull
	private static final PuzzleSolverFactory SINGLETON_INSTANCE = new PuzzleSolverFactory();

	/**
	 * Dieses Feld enthält die Menge der erzeugten {@link PuzzleSolver}.
	 *
	 * <p>
	 *     Die Menge wird automatisch um die vom Garbage-Collector zu entfernenden Instanzen
	 *     bereinigt.
	 * </p>
	 */
	@Nonnull
	private final Set<PuzzleSolver> instances = newSetFromMap(new WeakHashMap<>());

	/**
	 * Dieses Feld kann die auf neue Instanzen anzuwendende Aktion enthalten.
	 */
	@CheckForNull
	private Consumer<PuzzleSolver> action;

	/**
	 * Die Singleton-Instance wird per {@link #getSingletonInstance()} bereitgestellt.
	 */
	protected PuzzleSolverFactory() {
	}

	/**
	 * Diese Methode gibt die Singleton-Instance dieser Klasse zurück.
	 *
	 * @return die Instanz
	 */
	@Nonnull
	public static PuzzleSolverFactory getSingletonInstance() {
		return SINGLETON_INSTANCE;
	}

	/**
	 * Diese Methode erzeugt einen neuen {@link PuzzleSolver}.
	 *
	 * @return die erzeugte Instanz
	 */
	public synchronized PuzzleSolver create() {
		return this.create(0);
	}

	/**
	 * Diese Methode erzeugt einen neuen {@link PuzzleSolver}.
	 *
	 * @param delay das zu verwendende Delay in Millisekunden
	 * @return die erzeugte Instanz
	 */
	public synchronized PuzzleSolver create(final int delay) {
		PuzzleSolver newInstance = new PuzzleSolver(delay);

		if (this.action != null) {
			this.action.accept(newInstance);
		}

		this.instances.add(newInstance);
		return newInstance;
	}

	/**
	 * Diese Methode legt die Aktion für neue Instanzen fest und führt die Aktion auf den bisher
	 * erzeugten und noch vorhandenen Instanzen aus.
	 *
	 * @param newAction die festzulegende und auszuführende Aktion
	 */
	public synchronized void updateAction(@Nonnull final Consumer<PuzzleSolver> newAction) {
		this.action = newAction;
		this.instances.forEach(newAction);
	}
}
