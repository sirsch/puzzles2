package software.sirsch.sa4e.puzzles;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.apache.commons.collections4.iterators.PermutationIterator;

import static java.util.Spliterators.spliteratorUnknownSize;

/**
 * Diese Klasse stellt die Funktionalität zum Lösen eines Puzzles mittels Brute-Force-Ansatz.
 *
 * @author sirsch
 * @since 12.12.2022
 */
public class PuzzleSolver {

	/**
	 * Dieses Feld enthält das Delay zur Erzeugung der Permutationen in Millisekunden.
	 */
	private final int delay;

	/**
	 * Dieses Feld kann einen Logger zur Fortschrittsaufzeichnung enthalten.
	 */
	@Injectable
	@CheckForNull
	private volatile SolverProgressLogger solverProgressLogger;

	/**
	 * Dieser Konstruktor legt das Delay zur Verzögerung der Anwendung fest.
	 *
	 * @param delay das zu verwendende Delay in Millisekunden
	 */
	public PuzzleSolver(final int delay) {
		this.delay = delay;
	}

	/**
	 * Diese Methode legt den {@link SolverProgressLogger} fest.
	 *
	 * @param solverProgressLogger der zu setzende Logger oder {@code null} um den vormals gesetzten
	 * Logger zu entfernen
	 */
	public void setSolverProgressLogger(
			@CheckForNull final SolverProgressLogger solverProgressLogger) {

		this.solverProgressLogger = solverProgressLogger;
	}

	/**
	 * Diese Methode löst das Puzzle durch Durchprobieren von Permutationen.
	 *
	 * @param puzzle das zu lösende Puzzle
	 * @return die Symbole, mit gesetzten Werten oder eine leere Liste, falls keine Lösung gefunden
	 * wurde
	 */
	public List<Symbol> solvePuzzle(@Nonnull final Puzzle puzzle) {
		return this.createPermutationStreamWithDelay()
				.filter(this.createIsSolutionPredicate(puzzle))
				.findFirst()
				.map(permutation -> puzzle.getSymbols())
				.orElseGet(Collections::emptyList);
	}

	/**
	 * Diese Methode fügt dem {@link #createPermutationStream()} gegebenenfalls ein Delay hinzu.
	 *
	 * @return der resultierende {@link Stream}
	 */
	@Nonnull
	private Stream<List<Byte>> createPermutationStreamWithDelay() {
		Stream<List<Byte>> result = this.createPermutationStream();

		if (this.delay > 0) {
			result = result.map(this::delay);
		}

		return result;
	}

	/**
	 * Diese Methode verzögert die Rückgabe um {@link #delay} Millisekunden.
	 *
	 * @param permutation der zu verzögernde Wert
	 * @return der verzögerte Wert
	 */
	@Nonnull
	private List<Byte> delay(@Nonnull final List<Byte> permutation) {
		try {
			Thread.sleep(this.delay);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		return permutation;
	}

	/**
	 * Diese Methode erzeugt einen Stream der Permutationen der Ziffern.
	 *
	 * @return der erzeugte Stream
	 */
	@Nonnull
	private Stream<List<Byte>> createPermutationStream() {
		return StreamSupport.stream(this.createPermutationSpliterator(), false);
	}

	/**
	 * Diese Methode erzeugt einen Spliterator der Permutationen der Ziffern.
	 *
	 * @return der erzeugte Spliterator
	 */
	@Nonnull
	private Spliterator<List<Byte>> createPermutationSpliterator() {
		return spliteratorUnknownSize(this.createPermutationIterator(), Spliterator.ORDERED);
	}

	/**
	 * Diese Methode erzeugt einen Iterator der Permutationen der Ziffern.
	 *
	 * @return der erzeugte Iterator
	 */
	@Nonnull
	private Iterator<List<Byte>> createPermutationIterator() {
		return new PermutationIterator<>(this.createDigits());
	}

	/**
	 * Diese Methode erzeugt die Liste der Werte für die einzelnen Stellen von 0 bis 10.
	 *
	 * @return die erzeugte Liste
	 */
	@Nonnull
	private List<Byte> createDigits() {
		final int base = 10;

		return IntStream.range(0, base)
				.mapToObj(value -> (byte) value)
				.collect(Collectors.toList());
	}

	/**
	 * Diese Methode erzeugt ein {@link Predicate}, das prüft, ob die Permutation eine Lösung des
	 * Rätsels ist.
	 *
	 * @param puzzle das zu verwendende Puzzle
	 * @return das erzeugte Prädikat
	 */
	@Nonnull
	private Predicate<List<Byte>> createIsSolutionPredicate(@Nonnull final Puzzle puzzle) {
		return permutation -> {
			boolean isSolution = puzzle.isSolution(permutation);

			Optional.ofNullable(this.solverProgressLogger)
					.ifPresent(logger -> logger.logPermutation(puzzle.getSymbols(), isSolution));
			return isSolution;
		};
	}
}
