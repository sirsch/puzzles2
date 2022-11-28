package software.sirsch.sa4e.puzzles;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link Puzzle} bereit.
 *
 * @author sirsch
 * @since 27.11.2022
 */
public class PuzzleTest {

	/**
	 * Dieses Feld soll den Mock für das erste {@link Symbol} enthalten.
	 */
	private Symbol firstSymbol;

	/**
	 * Dieses Feld soll den Mock für die zweite {@link Symbol} enthalten.
	 */
	private Symbol secondSymbol;

	/**
	 * Dieses Feld soll den Mock für die erste {@link Addition} enthalten.
	 */
	private Addition firstAddition;

	/**
	 * Dieses Feld soll den Mock für die zweite {@link Addition} enthalten.
	 */
	private Addition secondAddition;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private Puzzle objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.firstSymbol = mock(Symbol.class);
		this.secondSymbol = mock(Symbol.class);
		this.firstAddition = mock(Addition.class);
		this.secondAddition = mock(Addition.class);

		this.objectUnderTest = new Puzzle(
				List.of(this.firstSymbol, this.secondSymbol),
				List.of(this.firstAddition, this.secondAddition));
	}

	/**
	 * Diese Methode prüft {@link Puzzle#getSymbols()}.
	 */
	@Test
	public void testGetSymbols() {
		List<Symbol> result;

		result = this.objectUnderTest.getSymbols();

		assertEquals(List.of(this.firstSymbol, this.secondSymbol), result);
		assertThrows(UnsupportedOperationException.class, () -> result.clear());
	}

	/**
	 * Diese Methode prüft {@link Puzzle#isSolution(List)}, wenn nicht genügend Werte übergeben
	 * wurden.
	 */
	@Test
	public void testIsSolution() {
		boolean result;

		when(this.firstAddition.isContradiction()).thenReturn(false);
		when(this.secondAddition.isContradiction()).thenReturn(false);

		result = this.objectUnderTest.isSolution(List.of((byte) 1, (byte) 2));

		assertTrue(result);
		verify(this.firstSymbol).bindValue((byte) 1);
		verify(this.secondSymbol).bindValue((byte) 2);
	}

	/**
	 * Diese Methode prüft {@link Puzzle#isSolution(List)}, wenn ein Widerspruch festgestellt wird.
	 */
	@Test
	public void testIsSolutionContradiction() {
		boolean result;

		when(this.firstAddition.isContradiction()).thenReturn(false);
		when(this.secondAddition.isContradiction()).thenReturn(true);

		result = this.objectUnderTest.isSolution(List.of((byte) 1, (byte) 2));

		assertFalse(result);
		verify(this.firstSymbol).bindValue((byte) 1);
		verify(this.secondSymbol).bindValue((byte) 2);
	}

	/**
	 * Diese Methode prüft {@link Puzzle#isSolution(List)}, wenn nicht genügend Werte übergeben
	 * wurden.
	 */
	@Test
	public void testIsSolutionNotEnoughValues() {
		boolean result;

		result = this.objectUnderTest.isSolution(emptyList());

		assertFalse(result);
	}

	/**
	 * Diese Methode prüft {@link Puzzle#isContradiction(List)}.
	 */
	@Test
	public void testIsContradiction() {
		boolean result;

		when(this.firstAddition.isContradiction()).thenReturn(true);

		result = this.objectUnderTest.isContradiction(List.of((byte) 1));

		assertTrue(result);
		verify(this.firstSymbol).bindValue((byte) 1);
		verify(this.secondSymbol).bindValue(null);
	}
}
