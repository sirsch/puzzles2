package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link Puzzle2CommonConverter} bereit.
 *
 * @author sirsch
 * @since 09.12.2022
 */
public class Puzzle2CommonConverterTest {

	/**
	 * Dieses Feld soll den Mock für das erste {@link Symbol} enthalten.
	 */
	private Symbol firstSymbol;

	/**
	 * Dieses Feld soll den Mock für das zweite {@link Symbol} enthalten.
	 */
	private Symbol secondSymbol;

	/**
	 * Dieses Feld soll den Mock für die erste {@link Cell} enthalten.
	 */
	private Cell firstCell;

	/**
	 * Dieses Feld soll den Mock für die zweite {@link Cell} enthalten.
	 */
	private Cell secondCell;

	/**
	 * Dieses Feld soll den Mock für das {@link Puzzle} enthalten.
	 */
	private Puzzle puzzle;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private Puzzle2CommonConverter objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.firstSymbol = mock(Symbol.class);
		this.secondSymbol = mock(Symbol.class);
		this.firstCell = mock(Cell.class);
		this.secondCell = mock(Cell.class);
		this.puzzle = mock(Puzzle.class);
		when(this.firstSymbol.getId()).thenReturn((int) 'A');
		when(this.firstSymbol.getDescription()).thenReturn("42");
		when(this.firstSymbol.getIconCodePoint()).thenReturn((int) 'A');
		when(this.secondSymbol.getId()).thenReturn((int) 'B');
		when(this.secondSymbol.getDescription()).thenReturn("13");
		when(this.secondSymbol.getIconCodePoint()).thenReturn((int) 'B');
		when(this.firstCell.getRow()).thenReturn(0);
		when(this.firstCell.getColumn()).thenReturn(1);
		when(this.firstCell.getSymbols()).thenReturn(List.of(this.firstSymbol, this.secondSymbol));
		when(this.secondCell.getRow()).thenReturn(1);
		when(this.secondCell.getColumn()).thenReturn(2);
		when(this.secondCell.getSymbols()).thenReturn(List.of(this.secondSymbol, this.firstSymbol));
		when(this.puzzle.getSymbols()).thenReturn(List.of(this.firstSymbol, this.secondSymbol));
		when(this.puzzle.getCells()).thenReturn(Set.of(this.firstCell, this.secondCell));

		this.objectUnderTest = new Puzzle2CommonConverter();
	}

	/**
	 * Diese Methode prüft {@link Puzzle2CommonConverter#createCommonSolvePuzzleRequest(Puzzle)}.
	 */
	@Test
	public void testCreateSolvePuzzleRequest() {
		CommonSolvePuzzleRequest result;

		result = this.objectUnderTest.createCommonSolvePuzzleRequest(this.puzzle);

		assertNotNull(result);
		assertEquals(List.of("", "BA", ""), result.getRow1());
		assertEquals(List.of("", "", "AB"), result.getRow2());
		assertEquals(List.of("", "", ""), result.getRow3());
	}
}
