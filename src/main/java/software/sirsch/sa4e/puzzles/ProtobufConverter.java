package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

/**
 * Diese Klasse stellt die Konvertierung von und nach Protobuf bereit.
 *
 * @author sirsch
 * @since 29.11.2022
 */
public class ProtobufConverter {

	/**
	 * Dieses Feld muss die Fabrik für {@link PuzzleBuilder} enthalten.
	 */
	@Nonnull
	private final Supplier<PuzzleBuilder> puzzleBuilderFactory;

	/**
	 * Dieses Feld muss die Fabrik für {@link Cell} enthalten.
	 */
	@Nonnull
	private final Function<List<Symbol>, Cell> cellFactory;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public ProtobufConverter() {
		this(PuzzleBuilder::new, Cell::new);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen der Fabriken zum Testen.
	 *
	 * @param puzzleBuilderFactory die zu setzende Fabrik für {@link PuzzleBuilder}
	 * @param cellFactory die zu setzende Fabrik für {@link Cell}
	 */
	protected ProtobufConverter(
			@Nonnull final  Supplier<PuzzleBuilder> puzzleBuilderFactory,
			@Nonnull final  Function<List<Symbol>, Cell> cellFactory) {

		this.puzzleBuilderFactory = puzzleBuilderFactory;
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
			return this.buildPuzzle(request, this.puzzleBuilderFactory.get()).build();
		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Diese Methode befüllt einen {@link PuzzleBuilder} mit den Daten aus der Anfrage.
	 *
	 * @param request die zu untersuchende Anfrage
	 * @param builder der zu befüllende Builder
	 * @return der befüllt Builder
	 */
	@Nonnull
	private PuzzleBuilder buildPuzzle(
			@Nonnull final Puzzles.SolvePuzzleRequest request,
			@Nonnull final PuzzleBuilder builder) {

		request.getSymbolsList().forEach(builder::findOrCreateSymbol);
		request.getCellsList().forEach(cell -> this.addCell(cell, builder));
		return builder;
	}

	/**
	 * Diese Methode übernimmt eine Zelle aus der Anfrage in den Builder.
	 *
	 * @param cell die zu übernehmende Zelle aus der Anfrage
	 * @param builder der zu befüllende Builder
	 * @return der Befüllte builder
	 */
	private PuzzleBuilder addCell(
			@Nonnull final Puzzles.Cell cell,
			@Nonnull final PuzzleBuilder builder) {

		return builder.withCell(
				cell.getRow(),
				cell.getColumn(),
				this.cellFactory.apply(cell.getNumberAsSymbolIdsList().stream()
						.map(symbolID -> builder.findOrCreateSymbol(symbolID, null))
						.collect(Collectors.toList())));
	}
}
