package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.Set;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link Puzzle2ProtobufConverter} bereit.
 *
 * @author sirsch
 * @since 09.12.2022
 */
public class Puzzle2ProtobufConverterTest {

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
	private Puzzle2ProtobufConverter objectUnderTest;

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
		when(this.firstSymbol.getId()).thenReturn(42);
		when(this.firstSymbol.getDescription()).thenReturn("42");
		when(this.firstSymbol.getIconCodePoint()).thenReturn((int) 'A');
		when(this.secondSymbol.getId()).thenReturn(13);
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

		this.objectUnderTest = new Puzzle2ProtobufConverter();
	}

	/**
	 * Diese Methode prüft {@link Puzzle2ProtobufConverter#createSolvePuzzleRequest(Puzzle)}.
	 */
	@Test
	public void testCreateSolvePuzzleRequest() {
		Puzzles.SolvePuzzleRequest result;

		result = this.objectUnderTest.createSolvePuzzleRequest(this.puzzle);

		assertThat(result, allOf(
				hasProperty("symbolsList", contains(
						allOf(
								hasProperty("id", equalTo(42)),
								hasProperty("description", equalTo("42")),
								hasProperty("iconCodePoint", equalTo((int) 'A'))),
						allOf(
								hasProperty("id", equalTo(13)),
								hasProperty("description", equalTo("13")),
								hasProperty("iconCodePoint", equalTo((int) 'B'))))),
				hasProperty("cellsList", containsInAnyOrder(
						allOf(
								hasProperty("row", equalTo(0)),
								hasProperty("column", equalTo(1)),
								hasProperty("numberAsSymbolIdsList", contains(42, 13))),
						allOf(
								hasProperty("row", equalTo(1)),
								hasProperty("column", equalTo(2)),
								hasProperty("numberAsSymbolIdsList", contains(13, 42)))
				))));
	}

	/**
	 * Diese Methode prüft {@link Puzzle2ProtobufConverter#createSolvePuzzleRequest(Puzzle)} wenn
	 * bei einem Symbol keine Beschreibung vorhanden ist.
	 */
	@Test
	public void testCreateSolvePuzzleRequestWithSymbolWithoutDescription() {
		Puzzles.SolvePuzzleRequest result;

		when(this.firstSymbol.getDescription()).thenReturn(null);

		result = this.objectUnderTest.createSolvePuzzleRequest(this.puzzle);

		assertThat(result, allOf(
				hasProperty("symbolsList", contains(
						allOf(
								hasProperty("id", equalTo(42)),
								hasProperty("description", equalTo("")),
								hasProperty("iconCodePoint", equalTo((int) 'A'))),
						allOf(
								hasProperty("id", equalTo(13)),
								hasProperty("description", equalTo("13")),
								hasProperty("iconCodePoint", equalTo((int) 'B'))))),
				hasProperty("cellsList", containsInAnyOrder(
						allOf(
								hasProperty("row", equalTo(0)),
								hasProperty("column", equalTo(1)),
								hasProperty("numberAsSymbolIdsList", contains(42, 13))),
						allOf(
								hasProperty("row", equalTo(1)),
								hasProperty("column", equalTo(2)),
								hasProperty("numberAsSymbolIdsList", contains(13, 42)))
				))));
	}
}