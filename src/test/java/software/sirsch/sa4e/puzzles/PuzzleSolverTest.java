package software.sirsch.sa4e.puzzles;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Diese Klasse stellt Tests für {@link PuzzleSolver} bereit.
 *
 * @author sirsch
 * @since 12.12.2022
 */
public class PuzzleSolverTest {

	/**
	 * Dieses Feld soll das Puzzle zum Testen enthalten.
	 */
	private Puzzle puzzle;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleSolver objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.puzzle = new PuzzleGenerator().generate(12, 34, 78, 56);

		this.objectUnderTest = new PuzzleSolver();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolver#solvePuzzle(Puzzle)}.
	 */
	@Test
	public void testSolvePuzzle() {
		List<Symbol> result;

		result = this.objectUnderTest.solvePuzzle(this.puzzle);

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
}
