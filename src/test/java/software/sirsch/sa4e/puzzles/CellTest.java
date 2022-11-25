package software.sirsch.sa4e.puzzles;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link Cell} bereit.
 *
 * @author sirsch
 * @since 25.11.2022
 */
public class CellTest {

	/**
	 * Dieses Fels soll den Mock für das {@link Symbol} an Stelle 0 enthalten.
	 */
	private Symbol symbol0;

	/**
	 * Dieses Fels soll den Mock für das {@link Symbol} an Stelle 1 enthalten.
	 */
	private Symbol symbol1;

	/**
	 * Dieses Fels soll den Mock für das {@link Symbol} an Stelle 2 enthalten.
	 */
	private Symbol symbol2;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private Cell objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.symbol0 = mock(Symbol.class);
		this.symbol1 = mock(Symbol.class);
		this.symbol2 = mock(Symbol.class);

		this.objectUnderTest = new Cell(List.of(this.symbol0, this.symbol1, this.symbol2));
	}

	/**
	 * Diese Methode prüft {@link Cell#areAllValuesBound()}, wenn allen Symbolen ein Wert gesetzt
	 * wurde.
	 */
	@Test
	public void testAreAllValuesBoundTrue() {
		boolean result;

		when(this.symbol0.isValueBound()).thenReturn(true);
		when(this.symbol1.isValueBound()).thenReturn(true);
		when(this.symbol2.isValueBound()).thenReturn(true);

		result = this.objectUnderTest.areAllValuesBound();

		assertTrue(result);
	}

	/**
	 * Diese Methode prüft {@link Cell#areAllValuesBound()}, wenn einem Symbol kein Wert gesetzt
	 * wurde.
	 */
	@Test
	public void testAreAllValuesBoundFalse() {
		boolean result;

		when(this.symbol0.isValueBound()).thenReturn(true);
		when(this.symbol1.isValueBound()).thenReturn(false);
		when(this.symbol2.isValueBound()).thenReturn(true);

		result = this.objectUnderTest.areAllValuesBound();

		assertFalse(result);
	}

	/**
	 * Diese Methode prüft {@link Cell#calculateValue()}.
	 */
	@Test
	public void testCalculateValue() {
		long result;

		when(this.symbol0.getBoundValue()).thenReturn((byte) 0);
		when(this.symbol1.getBoundValue()).thenReturn((byte) 1);
		when(this.symbol2.getBoundValue()).thenReturn((byte) 2);

		result = this.objectUnderTest.calculateValue();

		assertEquals(210L, result);
	}
}