package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * Diese Klasse stellt die Funktionalität zur konvertierung eines Puzzles in das
 * Austauschdatenmodell bereit.
 *
 * @author sirsch
 * @since 27.01.2023
 */
public class Puzzle2CommonConverter {

	/**
	 * Dieses Feld enthält die Zellen als Liste von Zeilen.
	 */
	@Nonnull
	private final List<List<StringBuilder>> rows = List.of(
			List.of(new StringBuilder(), new StringBuilder(), new StringBuilder()),
			List.of(new StringBuilder(), new StringBuilder(), new StringBuilder()),
			List.of(new StringBuilder(), new StringBuilder(), new StringBuilder()));

	/**
	 * Diese Methode erzeugt einen {@link CommonSolvePuzzleRequest} für ein {@link Puzzle}.
	 *
	 * @param puzzle das auszugebende Rätsel
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	public CommonSolvePuzzleRequest createCommonSolvePuzzleRequest(@Nonnull final Puzzle puzzle) {
		this.setCells(puzzle);
		return generateCommonSolvePuzzleRequest();
	}

	/**
	 * Diese Methode setzt die Zellen aus {@link #rows}.
	 *
	 * @param puzzle das zu untersuchende Puzzle
	 */
	private void setCells(@Nonnull final Puzzle puzzle) {
		this.clear();
		this.addCells(puzzle);
	}

	/**
	 * Diese Methode löscht die Zellen aus {@link #rows}.
	 */
	private void clear() {
		this.rows.forEach(column -> column.forEach(stringBuilder -> stringBuilder.setLength(0)));
	}

	/**
	 * Diese Methode fügt die Symbole der Zellen hinzu.
	 *
	 * @param puzzle das zu untersuchende Puzzle
	 */
	private void addCells(@Nonnull final Puzzle puzzle) {
		puzzle.getCells().forEach(this::addCell);
	}

	/**
	 * Diese Methode fügt die Symbole einer Zelle hinzu.
	 *
	 * @param cell die zu verwendende Zelle
	 */
	private void addCell(@Nonnull final Cell cell) {
		this.addSymbols(cell.getSymbols(), this.rows.get(cell.getRow()).get(cell.getColumn()));
	}

	/**
	 * Diese Methode fügt die Symbole einer Zelle in den {@link StringBuilder} ein.
	 *
	 * <p>
	 *     Dabei werden die Zeichen in umgekehrter Reihenfolge ausgegeben, weil im
	 *     Puzzle-Datenmodell die niederwertigste Stelle an Position 0 angeordnet ist.
	 * </p>
	 *
	 * @param symbols die zu verarbeitenden Symbole
	 * @param stringBuilder der zu befüllende StringBuilder
	 */
	private void addSymbols(
			@Nonnull final List<Symbol> symbols,
			@Nonnull final StringBuilder stringBuilder) {

		stringBuilder.setLength(0);
		symbols.stream()
				.map(Symbol::getId)
				.map(Character::toString)
				.forEach(stringBuilder::append);
		stringBuilder.reverse();
	}

	/**
	 * Diese Methode erzeugt aus den Inhalten von {@link #rows} einen
	 * {@link CommonSolvePuzzleRequest}.
	 *
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	private CommonSolvePuzzleRequest generateCommonSolvePuzzleRequest() {
		CommonSolvePuzzleRequest request = new CommonSolvePuzzleRequest();

		request.setRow1(this.generateRow(0));
		request.setRow2(this.generateRow(1));
		request.setRow3(this.generateRow(2));
		return request;
	}

	/**
	 * Diese Methode erzeugt eine Zeile als Liste von String.
	 *
	 * @param row die Zeilennummer
	 * @return die erzeugte Liste
	 */
	private List<String> generateRow(final int row) {
		return this.rows.get(row).stream()
				.map(StringBuilder::toString)
				.collect(Collectors.toList());
	}
}
