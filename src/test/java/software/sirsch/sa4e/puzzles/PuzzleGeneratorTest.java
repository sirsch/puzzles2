package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link PuzzleGenerator} bereit.
 *
 * <h1>Beispielrätsel</h1>
 * <pre>
 *     12 + 34 =  46
 *      +    +     +
 *     78 + 56 = 134
 *     _____________
 *     90 + 90 = 180
 * </pre>
 *
 * @author sirsch
 * @since 18.11.2022
 */
public class PuzzleGeneratorTest {

	/**
	 * Dieses Feld soll den Mock für {@link Random} enthalten.
	 */
	private Random random;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 0 ist, enthalten
	 */
	private Symbol symbol0;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 1 ist, enthalten
	 */
	private Symbol symbol1;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 2 ist, enthalten
	 */
	private Symbol symbol2;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 3 ist, enthalten
	 */
	private Symbol symbol3;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 4 ist, enthalten
	 */
	private Symbol symbol4;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 5 ist, enthalten
	 */
	private Symbol symbol5;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 6 ist, enthalten
	 */
	private Symbol symbol6;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 7 ist, enthalten
	 */
	private Symbol symbol7;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 8 ist, enthalten
	 */
	private Symbol symbol8;

	/**
	 * Dieses Feld soll den Mock für das {@link Symbol}, das im Test die 9 ist, enthalten
	 */
	private Symbol symbol9;

	/**
	 * Dieses Feld soll den Mock für {@link Puzzle} enthalten.
	 */
	private Puzzle puzzle;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleBuilder} enthalten.
	 */
	private PuzzleBuilder puzzleBuilder;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleGenerator objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.random = mock(Random.class);
		this.symbol0 = mock(Symbol.class);
		this.symbol1 = mock(Symbol.class);
		this.symbol2 = mock(Symbol.class);
		this.symbol3 = mock(Symbol.class);
		this.symbol4 = mock(Symbol.class);
		this.symbol5 = mock(Symbol.class);
		this.symbol6 = mock(Symbol.class);
		this.symbol7 = mock(Symbol.class);
		this.symbol8 = mock(Symbol.class);
		this.symbol9 = mock(Symbol.class);
		this.puzzle = mock(Puzzle.class);
		this.puzzleBuilder = mock(PuzzleBuilder.class);
		when(this.random.nextInt(100)).thenReturn(12, 34, 78, 56);
		when(this.puzzleBuilder.findOrCreateSymbol(1, null)).thenReturn(this.symbol2);
		when(this.puzzleBuilder.findOrCreateSymbol(2, null)).thenReturn(this.symbol1);
		when(this.puzzleBuilder.findOrCreateSymbol(3, null)).thenReturn(this.symbol4);
		when(this.puzzleBuilder.findOrCreateSymbol(4, null)).thenReturn(this.symbol3);
		when(this.puzzleBuilder.findOrCreateSymbol(5, null)).thenReturn(this.symbol6);
		when(this.puzzleBuilder.findOrCreateSymbol(6, null)).thenReturn(this.symbol8);
		when(this.puzzleBuilder.findOrCreateSymbol(7, null)).thenReturn(this.symbol7);
		when(this.puzzleBuilder.findOrCreateSymbol(8, null)).thenReturn(this.symbol5);
		when(this.puzzleBuilder.findOrCreateSymbol(9, null)).thenReturn(this.symbol0);
		when(this.puzzleBuilder.findOrCreateSymbol(10, null)).thenReturn(this.symbol9);
		when(this.puzzleBuilder.build()).thenReturn(this.puzzle);

		this.objectUnderTest = new PuzzleGenerator(
				this.random,
				this.puzzleBuilder,
				Cell::new);
	}

	/**
	 * Diese Methode prüft {@link PuzzleGenerator#PuzzleGenerator()}.
	 */
	@Test
	public void testDefaultConstructor() {
		Puzzle result;

		this.objectUnderTest = new PuzzleGenerator();

		result = this.objectUnderTest.generate(4);

		assertNotNull(result);
		assertNotNull(result.getCells());
		assertEquals(9, result.getCells().size());
	}

	/**
	 * Diese Methode prüft {@link PuzzleGenerator#generate(int)}.
	 */
	@Test
	public void testGenerate() {
		Puzzle result;

		result = this.objectUnderTest.generate(2);

		assertEquals(this.puzzle, result);
		verify(this.puzzleBuilder).withCell(
				this.cellThat(0, 0, this.symbol2, this.symbol1));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(0, 1, this.symbol4, this.symbol3));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(0, 2, this.symbol6, this.symbol4));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(1, 0, this.symbol8, this.symbol7));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(1, 1, this.symbol6, this.symbol5));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(1, 2, this.symbol4, this.symbol3, this.symbol1));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(2, 0, this.symbol0, this.symbol9));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(2, 1, this.symbol0, this.symbol9));
		verify(this.puzzleBuilder).withCell(
				this.cellThat(2, 2, this.symbol0, this.symbol8, this.symbol1));
	}

	/**
	 * Diese Methode erzeugt einen Mockito-Argument-Matcher für {@link Cell}s.
	 *
	 * @param row die erwartete Zeilennummer
	 * @param column die erwartete Spaltennummern
	 * @param symbols die erwarteten Symbole
	 * @return der erzeugte Matcher
	 */
	private Cell cellThat(final int row, final int column, final Symbol... symbols) {
		return argThat(cell -> cell != null
				&& cell.getRow() == row
				&& cell.getColumn() == column
				&& List.of(symbols).equals(cell.getSymbols()));
	}
}
