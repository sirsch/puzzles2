package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt die Konvertierung von {@link CommonSolvePuzzleRequest} nach {@link Puzzle}
 * bereit.
 *
 * @author sirsch
 * @since 26.01.2023
 */
public class Common2PuzzleConverter {

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
	public Common2PuzzleConverter() {
		this(new PuzzleBuilder(), Cell::new);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen der Fabriken zum Testen.
	 *
	 * @param puzzleBuilder der zu setzende {@link PuzzleBuilder}
	 * @param cellFactory die zu setzende Fabrik für {@link Cell}
	 */
	protected Common2PuzzleConverter(
			@Nonnull final PuzzleBuilder puzzleBuilder,
			@Nonnull final CellFactory cellFactory) {

		this.puzzleBuilder = puzzleBuilder;
		this.cellFactory = cellFactory;
	}

	/**
	 * Diese Methode erzeugt ein Puzzle zu einem {@link CommonSolvePuzzleRequest}.
	 *
	 * @param request die zu untersuchende Anfrage
	 * @return das erzeugte Puzzle
	 */
	@Nonnull
	public Puzzle createPuzzle(@Nonnull final CommonSolvePuzzleRequest request) {
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
	private void loadPuzzle(@Nonnull final CommonSolvePuzzleRequest request) {
		this.addRow(0, request.getRow1());
		this.addRow(1, request.getRow2());
		this.addRow(2, request.getRow3());
	}

	/**
	 * Diese Methode übernimmt eine Zeile aus der Anfrage in den Builder.
	 *
	 * @param row die Zeilennummer
	 * @param cells die zu untersuchende Zellen als Liste von Zeichenketten
	 */
	private void addRow(final int row, @CheckForNull final List<String> cells) {
		int column = 0;

		if (cells != null) {
			for (String cell : cells) {
				this.addCell(row, column++, cell);
			}
		}
	}

	/**
	 * Diese Methode übernimmt eine Zelle aus der Anfrage in den Builder.
	 *
	 * @param row die Zeilennummer
	 * @param column die Spaltennummer
	 * @param cell die zu untersuchende Zelle als Zeichenkette
	 */
	private void addCell(final int row, final int column, @Nonnull final String cell) {
		this.puzzleBuilder.withCell(this.convertCell(row, column, cell));
	}

	/**
	 * Diese Methode erzeugt eine {@link Cell} aus einer Zeichenkette.
	 *
	 * @param row die Zeilennummer
	 * @param column die Spaltennummer
	 * @param cell die zu untersuchende Zelle als Zeichenkette
	 * @return die erzeugte Zelle
	 */
	@Nonnull
	private Cell convertCell(final int row, final int column, @Nonnull final String cell) {
		return this.cellFactory.create(row, column, this.convertSymbols(cell));
	}

	/**
	 * Diese Methode erzeugt eine Symbolliste aus der Liste der Symbol-IDs.
	 *
	 * @param cell die zu untersuchende Zelle als Zeichenkette
	 * @return die erzeugte Liste
	 */
	@Nonnull
	private List<Symbol> convertSymbols(@Nonnull final String cell) {
		return cell.chars()
				.mapToObj(this::findSymbol)
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
		return this.puzzleBuilder.findOrCreateSymbol(
				symbolID,
				"Character '" + Character.toString(symbolID) + "'",
				symbolID);
	}
}
