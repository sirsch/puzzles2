package software.sirsch.sa4e.puzzles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link Common2PuzzleConverter} bereit.
 *
 * @author sirsch
 * @since 26.01.2023
 */
public class Common2PuzzleConverterTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private Common2PuzzleConverter objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new Common2PuzzleConverter();
	}

	/**
	 * Diese Methode prüft {@link Common2PuzzleConverter#createPuzzle(CommonSolvePuzzleRequest)}.
	 */
	@Test
	public void testCreatePuzzle() {
		CommonSolvePuzzleRequest request = mock(CommonSolvePuzzleRequest.class);
		Map<Character, Symbol> symbols = new HashMap<>();
		List<Symbol> ba;
		List<Symbol> cb;
		List<Symbol> dc;
		Puzzle result;

		when(request.getRow1()).thenReturn(List.of("AB", "AB", "BC"));
		when(request.getRow2()).thenReturn(List.of("AB", "AB", "BC"));
		when(request.getRow3()).thenReturn(List.of("BC", "BC", "CD"));

		result = this.objectUnderTest.createPuzzle(request);

		assertNotNull(result);
		result.getSymbols().forEach(symbol -> symbols.put((char) symbol.getId(), symbol));
		assertEquals(Set.of('A', 'B', 'C', 'D'), symbols.keySet());
		ba = List.of(symbols.get('B'), symbols.get('A'));
		cb = List.of(symbols.get('C'), symbols.get('B'));
		dc = List.of(symbols.get('D'), symbols.get('C'));
		assertThat(result.getCells(), containsInAnyOrder(
				allOf(
						hasProperty("row", equalTo(0)),
						hasProperty("column", equalTo(0)),
						hasProperty("symbols", equalTo(ba))),
				allOf(
						hasProperty("row", equalTo(0)),
						hasProperty("column", equalTo(1)),
						hasProperty("symbols", equalTo(ba))),
				allOf(
						hasProperty("row", equalTo(0)),
						hasProperty("column", equalTo(2)),
						hasProperty("symbols", equalTo(cb))),
				allOf(
						hasProperty("row", equalTo(1)),
						hasProperty("column", equalTo(0)),
						hasProperty("symbols", equalTo(ba))),
				allOf(
						hasProperty("row", equalTo(1)),
						hasProperty("column", equalTo(1)),
						hasProperty("symbols", equalTo(ba))),
				allOf(
						hasProperty("row", equalTo(1)),
						hasProperty("column", equalTo(2)),
						hasProperty("symbols", equalTo(cb))),
				allOf(
						hasProperty("row", equalTo(2)),
						hasProperty("column", equalTo(0)),
						hasProperty("symbols", equalTo(cb))),
				allOf(
						hasProperty("row", equalTo(2)),
						hasProperty("column", equalTo(1)),
						hasProperty("symbols", equalTo(cb))),
				allOf(
						hasProperty("row", equalTo(2)),
						hasProperty("column", equalTo(2)),
						hasProperty("symbols", equalTo(dc)))
				)
		);
	}

	/**
	 * Diese Methode prüft {@link Common2PuzzleConverter#createPuzzle(CommonSolvePuzzleRequest)}.
	 */
	@Test
	public void testCreatePuzzleIllegalStateException() {
		CommonSolvePuzzleRequest request = mock(CommonSolvePuzzleRequest.class);

		when(request.getRow1()).thenReturn(null);

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.createPuzzle(request));
	}
}
