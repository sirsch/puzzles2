package software.sirsch.sa4e.puzzles;

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
		Puzzles.SolvePuzzleRequest.Builder builder = Puzzles.SolvePuzzleRequest.newBuilder();

		puzzle.getSymbols().stream()
				.map(symbol -> Puzzles.Symbol.newBuilder()
						.setId(symbol.getId())
						.setDescription(symbol.getDescription())
						.build())
				.forEach(builder::addSymbols);
		puzzle.getCells().stream()
				.map(cell -> Puzzles.Cell.newBuilder()
						.setRow(cell.getRow())
						.setColumn(cell.getColumn())
						.addAllNumberAsSymbolIds(cell.getSymbols().stream()
								.map(Symbol::getId)
								.collect(Collectors.toList()))
						.build())
				.forEach(builder::addCells);
		return builder.build();
	}
}
