package software.sirsch.sa4e.puzzles;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt Tests für {@link PuzzleSolver} bereit.
 *
 * @author sirsch
 * @since 12.12.2022
 */
public class PuzzleSolverTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleSolver objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new PuzzleSolver();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolver#solvePuzzle(Puzzle)}, wenn das Puzzle mindestens eine
	 * Lösung hat.
	 */
	@Test
	public void testSolvePuzzleWithSolution() {
		Puzzle puzzle = new PuzzleGenerator().generate(12, 34, 78, 56);
		List<Symbol> result;

		result = this.objectUnderTest.solvePuzzle(puzzle);

		assertThat(result, contains(
				allOf(
						hasProperty("id", equalTo(1)),
						hasProperty("boundValue", equalTo((byte) 2))),
				allOf(
						hasProperty("id", equalTo(2)),
						hasProperty("boundValue", equalTo((byte) 1))),
				allOf(
						hasProperty("id", equalTo(3)),
						hasProperty("boundValue", equalTo((byte) 4))),
				allOf(
						hasProperty("id", equalTo(4)),
						hasProperty("boundValue", equalTo((byte) 3))),
				allOf(
						hasProperty("id", equalTo(5)),
						hasProperty("boundValue", equalTo((byte) 6))),
				allOf(
						hasProperty("id", equalTo(6)),
						hasProperty("boundValue", equalTo((byte) 8))),
				allOf(
						hasProperty("id", equalTo(7)),
						hasProperty("boundValue", equalTo((byte) 7))),
				allOf(
						hasProperty("id", equalTo(8)),
						hasProperty("boundValue", equalTo((byte) 5))),
				allOf(
						hasProperty("id", equalTo(9)),
						hasProperty("boundValue", equalTo((byte) 0))),
				allOf(
						hasProperty("id", equalTo(10)),
						hasProperty("boundValue", equalTo((byte) 9)))));
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolver#solvePuzzle(Puzzle)}, wenn das Puzzle mindestens
	 * keine Lösung hat.
	 */
	@Test
	public void testSolvePuzzleWithoutSolution() {
		PuzzleBuilder puzzleBuilder = new PuzzleBuilder();
		Symbol symbol0 = puzzleBuilder.findOrCreateSymbol(0, null, 0);
		Symbol symbol1 = puzzleBuilder.findOrCreateSymbol(1, null, 0);
		Puzzle puzzle;
		List<Symbol> result;

		puzzleBuilder.withCell(new Cell(0, 0, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 1, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 2, singletonList(symbol0)));
		puzzleBuilder.withCell(new Cell(1, 0, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(1, 1, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(1, 2, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 0, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 1, singletonList(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 2, singletonList(symbol1)));
		puzzle = puzzleBuilder.build();

		result = this.objectUnderTest.solvePuzzle(puzzle);

		assertEquals(emptyList(), result);
	}
}
