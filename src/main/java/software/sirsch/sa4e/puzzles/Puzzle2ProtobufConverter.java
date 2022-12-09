package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

/**
 * Diese Klasse stellt die Funktionalität zur Umwandlung von {@link Puzzle} nach Protobuf bereit.
 *
 * @author sirsch
 * @since 09.12.2022
 */
public class Puzzle2ProtobufConverter {

	/**
	 * Diese Methode erzeugt ein {@link Puzzles.SolvePuzzleRequest} zu einem Puzzle.
	 *
	 * @param puzzle das zu untersuchende Puzzle
	 * @return die erzeugte Anfrage
	 */
	@Nonnull
	public Puzzles.SolvePuzzleRequest createSolvePuzzleRequest(@Nonnull final Puzzle puzzle) {
		return this.fillBuilder(puzzle, Puzzles.SolvePuzzleRequest.newBuilder()).build();
	}

	/**
	 * Diese Methode befüllt den {@link Puzzles.SolvePuzzleRequest.Builder} mit dem Puzzle.
	 *
	 * @param puzzle das zu untersuchende Puzzle
	 * @param builder der zu befüllende Builder
	 * @return der befüllt Builder
	 */
	@Nonnull
	private Puzzles.SolvePuzzleRequest.Builder fillBuilder(
			@Nonnull final Puzzle puzzle,
			@Nonnull final Puzzles.SolvePuzzleRequest.Builder builder) {

		puzzle.getSymbols().stream()
				.map(this::convertSymbol)
				.forEach(builder::addSymbols);
		puzzle.getCells().stream()
				.map(this::convertCell)
				.forEach(builder::addCells);
		return builder;
	}

	/**
	 * Diese Methode überträgt ein {@link Symbol} in die Protobuf-Repräsentation.
	 *
	 * @param symbol das zu untersuchende Symbol
	 * @return das erzeugte Symbol
	 */
	@Nonnull
	private Puzzles.Symbol convertSymbol(@Nonnull final Symbol symbol) {
		return Puzzles.Symbol.newBuilder()
				.setId(symbol.getId())
				.setDescription(symbol.getDescription())
				.build();
	}

	/**
	 * Diese Methode überträgt eine {@link Cell} in die Protobuf-Repräsentation.
	 *
	 * @param cell die zu untersuchende Zelle
	 * @return die erzeugte Zelle
	 */
	@Nonnull
	private Puzzles.Cell convertCell(@Nonnull final Cell cell) {
		return Puzzles.Cell.newBuilder()
				.setRow(cell.getRow())
				.setColumn(cell.getColumn())
				.addAllNumberAsSymbolIds(this.convertSymbolsToIDs(cell.getSymbols()))
				.build();
	}

	/**
	 * Diese Methode konvertiert eine Symbolliste in eine Liste von IDs.
	 *
	 * @param symbols die zu untersuchende Liste
	 * @return die erzeugte Liste
	 */
	@Nonnull
	private List<Integer> convertSymbolsToIDs(@Nonnull final List<Symbol> symbols) {
		return symbols.stream()
				.map(Symbol::getId)
				.collect(Collectors.toList());
	}
}
