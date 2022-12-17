package software.sirsch.sa4e.puzzles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Diese Klasse stellt Tests für {@link Symbol} bereit.
 *
 * @author sirsch
 * @since 21.11.2022
 */
public class SymbolTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private Symbol objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new Symbol(42, "testDescription", 4242);
	}

	/**
	 * Diese Methode prüft {@link Symbol#bindValue(Byte)}.
	 */
	@Test
	public void testBindValue() {
		this.objectUnderTest.bindValue((byte) 7);

		assertTrue(this.objectUnderTest.isValueBound());
		assertEquals(7, this.objectUnderTest.getBoundValue());
	}

	/**
	 * Diese Methode prüft {@link Symbol#bindValue(Byte)} mit {@code null}.
	 */
	@Test
	public void testBindValueNull() {
		this.objectUnderTest.bindValue(null);

		assertFalse(this.objectUnderTest.isValueBound());
	}

	/**
	 * Diese Methode prüft {@link Symbol#bindValue(Byte)} mit einem zu niedrigen Wert.
	 */
	@Test
	public void testBindValueToLow() {
		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.bindValue((byte) -1));
	}

	/**
	 * Diese Methode prüft {@link Symbol#bindValue(Byte)} mit einem zu hohen Wert.
	 */
	@Test
	public void testBindValueToHigh() {
		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.bindValue((byte) 10));
	}

	/**
	 * Diese Methode prüft {@link Symbol#isValueBound()}.
	 */
	@Test
	public void testIsValueBound() {
		boolean result;

		result = this.objectUnderTest.isValueBound();

		assertFalse(result);
	}

	/**
	 * Diese Methode prüft {@link Symbol#getBoundValue()}, wenn kein Wert gebunden wurde.
	 */
	@Test
	public void testGetBoundValueNotBound() {
		assertThrows(IllegalStateException.class, () -> this.objectUnderTest.getBoundValue());
	}

	/**
	 * Diese Methode prüft {@link Symbol#getBoundValue()}.
	 */
	@Test
	public void testGetBoundValue() {
		byte result;

		this.objectUnderTest.bindValue((byte) 3);

		result = this.objectUnderTest.getBoundValue();

		assertEquals(3, result);
	}

	/**
	 * Diese Methode prüft {@link Symbol#getId()}.
	 */
	@Test
	public void testGetId() {
		int result;

		result = this.objectUnderTest.getId();

		assertEquals(42, result);
	}

	/**
	 * Diese Methode prüft {@link Symbol#getDescription()}.
	 */
	@Test
	public void testGetDescription() {
		String result;

		result = this.objectUnderTest.getDescription();

		assertEquals("testDescription", result);
	}

	/**
	 * Diese Methode prüft {@link Symbol#getIconCodePoint()}.
	 */
	@Test
	public void testGetIconCodePoint() {
		int result;

		result = this.objectUnderTest.getIconCodePoint();

		assertEquals(4242, result);
	}
}
