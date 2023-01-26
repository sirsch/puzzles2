package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

/**
 * Diese Klasse stellt die Konvertierung von Protobuf in das {@link Puzzle}-Datenformat bereit.
 *
 * @author sirsch
 * @since 29.11.2022
 */
public class Protobuf2PuzzleConverter {

	/**
	 * Dieses Feld muss den {@link PuzzleBuilder} enthalten.
	 */
	@Nonnull
	private final PuzzleBuilder puzzleBuilder;

	/**
	 * Dieses Feld muss die Fabrik für {@link Cell} enthalten.
	 */
	@Nonnull
	private final CellFactory cellFactory;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public Protobuf2PuzzleConverter() {
		this(new PuzzleBuilder(), Cell::new);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen der Fabriken zum Testen.
	 *
	 * @param puzzleBuilder der zu setzende {@link PuzzleBuilder}
	 * @param cellFactory die zu setzende Fabrik für {@link Cell}
	 */
	protected Protobuf2PuzzleConverter(
			@Nonnull final PuzzleBuilder puzzleBuilder,
			@Nonnull final CellFactory cellFactory) {

		this.puzzleBuilder = puzzleBuilder;
		this.cellFactory = cellFactory;
	}

	/**
	 * Diese Methode erzeugt ein Puzzle zu einem {@link Puzzles.SolvePuzzleRequest}.
	 *
	 * @param request die zu untersuchende Anfrage
	 * @return das erzeugte Puzzle
	 */
	@Nonnull
	public Puzzle createPuzzle(@Nonnull final Puzzles.SolvePuzzleRequest request) {
		try {
			this.loadPuzzle(request);
			return this.puzzleBuilder.build();
		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Diese Methode befüllt einen {@link PuzzleBuilder} mit den Daten aus der Anfrage.
	 *
	 * @param request die zu untersuchende Anfrage
	 */
	private void loadPuzzle(@Nonnull final Puzzles.SolvePuzzleRequest request) {
		request.getSymbolsList().forEach(this.puzzleBuilder::findOrCreateSymbol);
		request.getCellsList().forEach(this::addCell);
	}

	/**
	 * Diese Methode übernimmt eine Zelle aus der Anfrage in den Builder.
	 *
	 * @param cell die zu übernehmende Zelle aus der Anfrage
	 */
	private void addCell(@Nonnull final Puzzles.Cell cell) {
		this.puzzleBuilder.withCell(this.convertCell(cell));
	}

	/**
	 * Diese Methode erzeugt eine {@link Cell} aus einer {@link Puzzles.Cell}.
	 *
	 * @param cell die zu untersuchende Zelle
	 * @return die erzeugte Zelle
	 */
	@Nonnull
	private Cell convertCell(@Nonnull final Puzzles.Cell cell) {
		return this.cellFactory.create(
				cell.getRow(),
				cell.getColumn(),
				this.convertSymbols(cell.getNumberAsSymbolIdsList()));
	}

	/**
	 * Diese Methode erzeugt eine Symbolliste aus der Liste der Symbol-IDs.
	 *
	 * @param symbolIds die auszuwertenden Symbol-IDs
	 * @return die erzeugte Liste
	 */
	@Nonnull
	private List<Symbol> convertSymbols(@Nonnull final List<Integer> symbolIds) {
		return symbolIds.stream()
				.map(this::findSymbol)
				.collect(Collectors.toList());
	}

	/**
	 * Diese Methode sucht das Symbol zu einer ID.
	 *
	 * @param symbolID die ID des zu suchenden Symbols
	 * @return das ermittelte Symbol
	 */
	@Nonnull
	private Symbol findSymbol(final int symbolID) {
		return this.puzzleBuilder.findOrCreateSymbol(symbolID, null, 0);
	}
}
