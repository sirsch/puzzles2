package software.sirsch.sa4e.puzzles;

import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.iterators.PermutationIterator;

/**
 * Diese Klasse stellt die Funktionalität zum Lösen eines Puzzles mittels Brute-Force-Ansatz.
 *
 * @author sirsch
 * @since 12.12.2022
 */
public class PuzzleSolver {

	/**
	 * Diese Methode löst das Puzzle durch Durchprobieren von Permutationen.
	 *
	 * @param puzzle das zu lösende Puzzle
	 * @return die Symbole, mit gesetzten Werten oder eine leere Liste, falls keine Lösung gefunden
	 * wurde
	 */
	public List<Symbol> solvePuzzle(@Nonnull final Puzzle puzzle) {
		final int base = 10;

		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(
						new PermutationIterator<>(IntStream.range(0, base)
								.mapToObj(value -> (byte) value)
								.collect(Collectors.toList())),
						Spliterator.ORDERED),
				false)
				.filter(puzzle::isSolution)
				.findFirst()
				.map(permutation -> puzzle.getSymbols())
				.orElseGet(Collections::emptyList);
	}
}
