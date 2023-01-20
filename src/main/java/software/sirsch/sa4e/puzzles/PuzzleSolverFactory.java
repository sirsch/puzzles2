package software.sirsch.sa4e.puzzles;

import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.Factory;

import static java.util.Collections.newSetFromMap;

/**
 * Diese Klasse stellt eine Fabrik für {@link PuzzleSolver} bereit.
 *
 * @author sirsch
 * @since 20.01.2023
 */
public class PuzzleSolverFactory implements Factory<PuzzleSolver> {

	/**
	 * Dieses Feld soll die Singleton-Instance dieser Klasse enthalten.
	 */
	@Nonnull
	private static final PuzzleSolverFactory SINGLETON_INSTANCE = new PuzzleSolverFactory();

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
	 * Diese Methode erzeugt einen neuen {@link PuzzleSolver}.
	 *
	 * @return die erzeugte Instanz
	 */
	@Override
	public synchronized PuzzleSolver create() {
		PuzzleSolver newInstance = new PuzzleSolver();

		this.instances.add(newInstance);
		return newInstance;
	}

	/**
	 * Diese Methode führt eine Aktion auf den bisher erzeugten und noch vorhandenen
	 * {@link PuzzleSolver} aus.
	 *
	 * @param action die auszuführende Aktion
	 */
	public synchronized void forEach(@Nonnull final Consumer<PuzzleSolver> action) {
		this.instances.forEach(action);
	}
}
