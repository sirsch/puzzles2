package software.sirsch.sa4e.puzzles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link Addition} bereit.
 *
 * @author sirsch
 * @since 25.11.2022
 */
public class AdditionTest {

	/**
	 * Dieses Feld soll den Mock für die {@link Cell} des ersten Summanden enthalten.
	 */
	private Cell firstSummand;

	/**
	 * Dieses Feld soll den Mock für die {@link Cell} des zweiten Summanden enthalten.
	 */
	private Cell secondSummand;

	/**
	 * Dieses Feld soll den Mock für die {@link Cell} der Summe enthalten.
	 */
	private Cell sum;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private Addition objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.firstSummand = mock(Cell.class);
		this.secondSummand = mock(Cell.class);
		this.sum = mock(Cell.class);

		this.objectUnderTest = new Addition(this.firstSummand, this.secondSummand, this.sum);
	}

	/**
	 * Diese Methode prüft {@link Addition#isFalse()}, wenn die Zelle des ersten Summanden ein nicht
	 * gebundenes Symbol enthält.
	 */
	@Test
	public void testIsFalseFirstSummandNotBound() {
		boolean result;

		when(this.firstSummand.areAllValuesBound()).thenReturn(false);

		result = this.objectUnderTest.isFalse();

		assertFalse(result);
	}

	/**
	 * Diese Methode prüft {@link Addition#isFalse()}, wenn die Zelle des zweiten Summanden ein
	 * nicht gebundenes Symbol enthält.
	 */
	@Test
	public void testIsFalseSecondSummandNotBound() {
		boolean result;

		when(this.firstSummand.areAllValuesBound()).thenReturn(true);
		when(this.secondSummand.areAllValuesBound()).thenReturn(false);

		result = this.objectUnderTest.isFalse();

		assertFalse(result);
	}

	/**
	 * Diese Methode prüft {@link Addition#isFalse()}, wenn die Zelle der Summe ein nicht gebundenes
	 * Symbol enthält.
	 */
	@Test
	public void testIsFalseSumNotBound() {
		boolean result;

		when(this.firstSummand.areAllValuesBound()).thenReturn(true);
		when(this.secondSummand.areAllValuesBound()).thenReturn(true);
		when(this.sum.areAllValuesBound()).thenReturn(false);

		result = this.objectUnderTest.isFalse();

		assertFalse(result);
	}

	/**
	 * Diese Methode prüft {@link Addition#isFalse()}, wenn die Zelle der Summe ein nicht gebundenes
	 * Symbol enthält.
	 */
	@Test
	public void testIsFalseSumCorrect() {
		boolean result;

		when(this.firstSummand.areAllValuesBound()).thenReturn(true);
		when(this.secondSummand.areAllValuesBound()).thenReturn(true);
		when(this.sum.areAllValuesBound()).thenReturn(true);
		when(this.firstSummand.calculateValue()).thenReturn(1L);
		when(this.secondSummand.calculateValue()).thenReturn(2L);
		when(this.sum.calculateValue()).thenReturn(3L);

		result = this.objectUnderTest.isFalse();

		assertFalse(result);
	}

	/**
	 * Diese Methode prüft {@link Addition#isFalse()}, wenn die Zelle der Summe ein nicht gebundenes
	 * Symbol enthält.
	 */
	@Test
	public void testIsFalseSumIncorrect() {
		boolean result;

		when(this.firstSummand.areAllValuesBound()).thenReturn(true);
		when(this.secondSummand.areAllValuesBound()).thenReturn(true);
		when(this.sum.areAllValuesBound()).thenReturn(true);
		when(this.firstSummand.calculateValue()).thenReturn(4L);
		when(this.secondSummand.calculateValue()).thenReturn(2L);
		when(this.sum.calculateValue()).thenReturn(42L);

		result = this.objectUnderTest.isFalse();

		assertTrue(result);
	}
}
