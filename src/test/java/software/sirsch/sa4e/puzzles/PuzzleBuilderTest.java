package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link PuzzleBuilder} bereit.
 *
 * @author sirsch
 * @since 23.11.2022
 */
public class PuzzleBuilderTest {

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 0 Spalte 0 enthalten.
	 */
	private Cell cell00;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 0 Spalte 1 enthalten.
	 */
	private Cell cell01;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 0 Spalte 2 enthalten.
	 */
	private Cell cell02;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 1 Spalte 0 enthalten.
	 */
	private Cell cell10;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 1 Spalte 1 enthalten.
	 */
	private Cell cell11;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 1 Spalte 2 enthalten.
	 */
	private Cell cell12;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 2 Spalte 0 enthalten.
	 */
	private Cell cell20;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 2 Spalte 1 enthalten.
	 */
	private Cell cell21;

	/**
	 * Dieses Feld soll den Mock für die Zelle Zeile 2 Spalte 2 enthalten.
	 */
	private Cell cell22;

	/**
	 * Dieses Feld soll den Mock für die Addition aus Zeile 0 enthalten.
	 */
	private Addition additionRow0;

	/**
	 * Dieses Feld soll den Mock für die Addition aus Zeile 1 enthalten.
	 */
	private Addition additionRow1;

	/**
	 * Dieses Feld soll den Mock für die Addition aus Zeile 2 enthalten.
	 */
	private Addition additionRow2;

	/**
	 * Dieses Feld soll den Mock für die Addition aus Spalte 0 enthalten.
	 */
	private Addition additionColumn0;

	/**
	 * Dieses Feld soll den Mock für die Addition aus Spalte 1 enthalten.
	 */
	private Addition additionColumn1;

	/**
	 * Dieses Feld soll den Mock für die Addition aus Spalte 2 enthalten.
	 */
	private Addition additionColumn2;

	/**
	 * Dieses Feld soll den Mock für {@link Puzzle} enthalten.
	 */
	private Puzzle puzzle;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleFactory} enthalten.
	 */
	private PuzzleFactory puzzleFactory;

	/**
	 * Dieses Feld soll den Mock für {@link AdditionFactory} enthalten.
	 */
	private AdditionFactory additionFactory;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleBuilder objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.cell00 = mock(Cell.class);
		this.cell01 = mock(Cell.class);
		this.cell02 = mock(Cell.class);
		this.cell10 = mock(Cell.class);
		this.cell11 = mock(Cell.class);
		this.cell12 = mock(Cell.class);
		this.cell20 = mock(Cell.class);
		this.cell21 = mock(Cell.class);
		this.cell22 = mock(Cell.class);
		this.additionRow0 = mock(Addition.class);
		this.additionRow1 = mock(Addition.class);
		this.additionRow2 = mock(Addition.class);
		this.additionColumn0 = mock(Addition.class);
		this.additionColumn1 = mock(Addition.class);
		this.additionColumn2 = mock(Addition.class);
		this.puzzle = mock(Puzzle.class);
		this.puzzleFactory = mock(PuzzleFactory.class);
		this.additionFactory = mock(AdditionFactory.class);
		when(this.cell00.getRow()).thenReturn(0);
		when(this.cell00.getColumn()).thenReturn(0);
		when(this.cell01.getRow()).thenReturn(0);
		when(this.cell01.getColumn()).thenReturn(1);
		when(this.cell02.getRow()).thenReturn(0);
		when(this.cell02.getColumn()).thenReturn(2);
		when(this.cell10.getRow()).thenReturn(1);
		when(this.cell10.getColumn()).thenReturn(0);
		when(this.cell11.getRow()).thenReturn(1);
		when(this.cell11.getColumn()).thenReturn(1);
		when(this.cell12.getRow()).thenReturn(1);
		when(this.cell12.getColumn()).thenReturn(2);
		when(this.cell20.getRow()).thenReturn(2);
		when(this.cell20.getColumn()).thenReturn(0);
		when(this.cell21.getRow()).thenReturn(2);
		when(this.cell21.getColumn()).thenReturn(1);
		when(this.cell22.getRow()).thenReturn(2);
		when(this.cell22.getColumn()).thenReturn(2);
		when(this.additionFactory.create(this.cell00, this.cell01, this.cell02))
				.thenReturn(this.additionRow0);
		when(this.additionFactory.create(this.cell10, this.cell11, this.cell12))
				.thenReturn(this.additionRow1);
		when(this.additionFactory.create(this.cell20, this.cell21, this.cell22))
				.thenReturn(this.additionRow2);
		when(this.additionFactory.create(this.cell00, this.cell10, this.cell20))
				.thenReturn(this.additionColumn0);
		when(this.additionFactory.create(this.cell01, this.cell11, this.cell21))
				.thenReturn(this.additionColumn1);
		when(this.additionFactory.create(this.cell02, this.cell12, this.cell22))
				.thenReturn(this.additionColumn2);

		this.objectUnderTest = new PuzzleBuilder(this.puzzleFactory, this.additionFactory);
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#withCell(Cell)}.
	 */
	@Test
	public void testWithCell() {
		PuzzleBuilder result;

		result = this.objectUnderTest.withCell(this.cell00);

		assertEquals(this.objectUnderTest, result);
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#withCell(Cell)}, wenn die Zeilennummer
	 * zu klein ist.
	 */
	@Test
	public void testWithCellRowNumberToLow() {
		Cell cell = mock(Cell.class);

		when(cell.getRow()).thenReturn(-1);

		assertThrows(IllegalArgumentException.class, () -> this.objectUnderTest.withCell(cell));
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#withCell(Cell)}, wenn die Zeilennummer
	 * zu groß ist.
	 */
	@Test
	public void testWithCellRowNumberToHigh() {
		Cell cell = mock(Cell.class);

		when(cell.getRow()).thenReturn(3);

		assertThrows(IllegalArgumentException.class, () -> this.objectUnderTest.withCell(cell));
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#withCell(Cell)}, wenn die Spaltennummer
	 * zu klein ist.
	 */
	@Test
	public void testWithCellColumnNumberToLow() {
		Cell cell = mock(Cell.class);

		when(cell.getColumn()).thenReturn(-1);

		assertThrows(IllegalArgumentException.class, () -> this.objectUnderTest.withCell(cell));
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#withCell(Cell)}, wenn die Spaltennummer
	 * zu groß ist.
	 */
	@Test
	public void testWithCellColumnNumberToHigh() {
		Cell cell = mock(Cell.class);

		when(cell.getColumn()).thenReturn(3);

		assertThrows(IllegalArgumentException.class, () -> this.objectUnderTest.withCell(cell));
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#withCell(Cell)}, wenn die Zelle ein
	 * ungültiges Symbol enthält.
	 */
	@Test
	public void testWithCellInvalidSymbol() {
		when(this.cell00.getSymbols()).thenReturn(List.of(mock(Symbol.class)));

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.withCell(this.cell00));
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#findOrCreateSymbol(Puzzles.Symbol)}.
	 */
	@Test
	public void testFindOrCreateSymbol() {
		Puzzles.Symbol symbol = Puzzles.Symbol.newBuilder()
				.setId(42)
				.setDescription("testDescription")
				.build();
		Symbol result;

		result = this.objectUnderTest.findOrCreateSymbol(symbol);

		assertNotNull(result);
		assertEquals(42, result.getId());
		assertEquals("testDescription", result.getDescription());
		assertFalse(result.isValueBound());
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#findOrCreateSymbol(int, String)}, dass bei mehreren
	 * Aufrufen mit derselben ID dieselbe Instanz zurückgegeben wird.
	 */
	@Test
	public void testFindOrCreateSymbolRepeatedCallReturnsSameInstance() {
		Symbol firstResult;
		Symbol secondResult;

		firstResult = this.objectUnderTest.findOrCreateSymbol(42, "testDescription");
		secondResult = this.objectUnderTest.findOrCreateSymbol(42, "otherDescription");

		assertNotNull(firstResult);
		assertSame(firstResult, secondResult);
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#findOrCreateSymbol(int, String)}, dass nicht mehr
	 * als 10 Symbole erzeugt werden können.
	 */
	@Test
	public void testFindOrCreateSymbolToManySymbols() {
		assertThrows(
				IllegalStateException.class,
				() -> IntStream.range(0, 11).forEach(
						id -> this.objectUnderTest.findOrCreateSymbol(id, null)));
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#build()}.
	 */
	@Test
	public void testBuild() {
		Symbol firstSymbol;
		Symbol secondSymbol;
		Puzzle result;

		firstSymbol = this.objectUnderTest.findOrCreateSymbol(42, "firstSymbol");
		secondSymbol = this.objectUnderTest.findOrCreateSymbol(13, "secondSymbol");
		when(this.cell00.getSymbols()).thenReturn(List.of(firstSymbol, secondSymbol));
		when(this.puzzleFactory.create(List.of(firstSymbol, secondSymbol), this.listAdditions()))
				.thenReturn(this.puzzle);
		this.objectUnderTest.withCell(this.cell00);
		this.objectUnderTest.withCell(this.cell01);
		this.objectUnderTest.withCell(this.cell02);
		this.objectUnderTest.withCell(this.cell10);
		this.objectUnderTest.withCell(this.cell11);
		this.objectUnderTest.withCell(this.cell12);
		this.objectUnderTest.withCell(this.cell20);
		this.objectUnderTest.withCell(this.cell21);
		this.objectUnderTest.withCell(this.cell22);

		result = this.objectUnderTest.build();

		assertEquals(this.puzzle, result);
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#build()}, wenn mindestens eine Zelle fehlt.
	 */
	@Test
	public void testBuildMissingCell() {
		assertThrows(IllegalStateException.class, () -> this.objectUnderTest.build());
	}

	/**
	 * Diese Methode prüft {@link PuzzleBuilder#PuzzleBuilder()}.
	 */
	@Test
	public void testDefaultConstructor() {
		Symbol firstSymbol;
		Symbol secondSymbol;

		this.objectUnderTest = new PuzzleBuilder();

		firstSymbol = this.objectUnderTest.findOrCreateSymbol(42, "firstSymbol");
		secondSymbol = this.objectUnderTest.findOrCreateSymbol(13, "secondSymbol");
		when(this.cell00.getSymbols()).thenReturn(List.of(firstSymbol, secondSymbol));
		this.objectUnderTest.withCell(this.cell00);
		this.objectUnderTest.withCell(this.cell01);
		this.objectUnderTest.withCell(this.cell02);
		this.objectUnderTest.withCell(this.cell10);
		this.objectUnderTest.withCell(this.cell11);
		this.objectUnderTest.withCell(this.cell12);
		this.objectUnderTest.withCell(this.cell20);
		this.objectUnderTest.withCell(this.cell21);
		this.objectUnderTest.withCell(this.cell22);
		assertNotNull(this.objectUnderTest.build());
	}

	/**
	 * Diese Methode listet die Mocks der Gleichungen auf.
	 *
	 * @return die erzeugte Liste
	 */
	@Nonnull
	private List<Addition> listAdditions() {
		return List.of(
				this.additionRow0,
				this.additionRow1,
				this.additionRow2,
				this.additionColumn0,
				this.additionColumn1,
				this.additionColumn2);
	}
}