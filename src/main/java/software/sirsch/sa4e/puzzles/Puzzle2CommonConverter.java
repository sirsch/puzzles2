package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.iterators.ReverseListIterator;

/**
 * Diese Klasse stellt die Funktionalität zur konvertierung eines Puzzles in das
 * Austauschdatenmodell bereit.
 *
 * @author sirsch
 * @since 27.01.2023
 */
public class Puzzle2CommonConverter {

	/**
	 * Dieses Feld enthält die Zellen als Liste von Spalten.
	 */
	@Nonnull
	private final List<List<StringBuilder>> columns = List.of(
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
	public CommonSolvePuzzleRequest print(@Nonnull final Puzzle puzzle) {
		this.setCells(puzzle);
		return null;
	}

	/**
	 * Diese Methode setzt die Zellen aus {@link #columns}.
	 *
	 * @param puzzle das zu untersuchende Puzzle
	 */
	private void setCells(@Nonnull final Puzzle puzzle) {
		this.clear();
		this.addCells(puzzle);
	}

	/**
	 * Diese Methode löscht die Zellen aus {@link #columns}.
	 */
	private void clear() {
		this.columns.forEach(column -> column.forEach(stringBuilder -> stringBuilder.setLength(0)));
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
		this.addSymbols(cell.getSymbols(), this.columns.get(cell.getRow()).get(cell.getColumn()));
	}

	/**
	 * Diese Methode fügt die Symbole einer Zelle in den {@link StringBuilder} ein.
	 *
	 * <p>
	 *     Dabei werden die Zeichen in umgekehrter Reihenfolge ausgegeben, weil im
	 *     Puzzle-Datenmodell die niederwertigste Stelle an Position 0 angeordnet ist.
	 * </p>
	 *
	 * @param symbols
	 * @param stringBuilder
	 */
	private void addSymbols(
			@Nonnull final List<Symbol> symbols,
			@Nonnull final StringBuilder stringBuilder) {

		symbols.stream()
				.map(Symbol::getId)
				.map(Character::toString)
				.forEach(stringBuilder::append);
		stringBuilder.reverse();
	}
}
