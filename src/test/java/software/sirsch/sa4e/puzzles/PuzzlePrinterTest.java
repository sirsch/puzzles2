package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

/**
 * Diese Klasse stellt Tests für {@link PuzzlePrinter} bereit.
 *
 * @author sirsch
 * @since 18.12.2022
 */
public class PuzzlePrinterTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzlePrinter objectUnderTest;

	/**
	 * Dieses Feld soll den Mock für die Ausgabe enthalten.
	 */
	private PrintStream out;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.out = mock(PrintStream.class);

		this.objectUnderTest = new PuzzlePrinter(this.out);
	}

	/**
	 * Diese Methode prüft {@link PuzzlePrinter#print(Puzzle)}.
	 */
	@Test
	public void testPrint() {
		InOrder orderVerifier = inOrder(this.out);

		this.objectUnderTest.print(this.generatePuzzle());

		orderVerifier.verify(this.out).println(" A + A =   A");
		orderVerifier.verify(this.out).println("AB + B =   B");
		orderVerifier.verify(this.out).println(" B + B = BAB");
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode erzeugt ein einfaches Puzzle mit einem Symbol für 0.
	 *
	 * @return das erzeugte Puzzle
	 */
	@Nonnull
	private Puzzle generatePuzzle() {
		PuzzleBuilder puzzleBuilder = new PuzzleBuilder();
		Symbol symbol0 = puzzleBuilder.findOrCreateSymbol(0, null, 'A');
		Symbol symbol1 = puzzleBuilder.findOrCreateSymbol(1, null, 'B');

		puzzleBuilder.withCell(new Cell(0, 0, List.of(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 1, List.of(symbol0)));
		puzzleBuilder.withCell(new Cell(0, 2, List.of(symbol0)));
		puzzleBuilder.withCell(new Cell(1, 0, List.of(symbol1, symbol0)));
		puzzleBuilder.withCell(new Cell(1, 1, List.of(symbol1)));
		puzzleBuilder.withCell(new Cell(1, 2, List.of(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 0, List.of(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 1, List.of(symbol1)));
		puzzleBuilder.withCell(new Cell(2, 2, List.of(symbol1, symbol0, symbol1)));
		return puzzleBuilder.build();
	}
}
