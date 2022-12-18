package software.sirsch.sa4e.puzzles;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.iterators.ReverseListIterator;

/**
 * Diese Klasse stellt die Funktionalität zur textbasierten Ausgabe eines Puzzles bereit.
 *
 * @author sirsch
 * @since 18.12.2022
 */
public class PuzzlePrinter {

	/**
	 * Dieses Feld enthält die Zellen als Liste von Spalten.
	 */
	@Nonnull
	private final List<List<List<Symbol>>> columns = List.of(
			List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
			List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
			List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

	/**
	 * Dieses Feld muss den zu beschreibenden {@link PrintStream} für die Ausgabe enthalten.
	 */
	@Nonnull
	private final PrintStream out;

	/**
	 * Dieser Konstruktor führt die interne Initialisierung durch.
	 */
	public PuzzlePrinter() {
		this(System.out);
	}

	/**
	 * Dieser Konstruktor legt den für die Ausgabe zu verwendenden {@link PrintStream} fest.
	 *
	 * @param out der zu setzende {@link PrintStream}
	 */
	public PuzzlePrinter(@Nonnull final PrintStream out) {
		this.out = out;
	}

	/**
	 * Diese Methode gibt ein Rätsel in einer lesbaren Darstellung aus.
	 *
	 * @param puzzle das auszugebende Rätsel
	 */
	public void print(@Nonnull final Puzzle puzzle) {
		this.setCells(puzzle);
		IntStream.range(0, this.columns.get(0).size()).forEach(this::printRow);
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
		this.columns.forEach(column -> column.forEach(List::clear));
	}

	/**
	 * Diese Methode fügt die Zellen-Symbole hinzu.
	 *
	 * @param puzzle das zu untersuchende Puzzle
	 */
	private void addCells(@Nonnull final Puzzle puzzle) {
		puzzle.getCells().forEach(
				cell -> this.columns.get(cell.getColumn())
						.get(cell.getRow())
						.addAll(cell.getSymbols()));
	}

	/**
	 * Methode gibt eine Zeile des Rätsels aus.
	 *
	 * @param row die Nummer der auszugebenden Zeile
	 */
	private void printRow(final int row) {
		this.out.println(this.generateRow(row, new StringBuilder()).toString());
	}

	/**
	 * Diese Methode erzeugt die Zeile in einem {@link StringBuilder}.
	 *
	 * @param row die Nummer der Zeile
	 * @param stringBuilder der zu verwendende {@link StringBuilder}
	 * @return der verwendete {@link StringBuilder}
	 */
	private StringBuilder generateRow(final int row, @Nonnull final StringBuilder stringBuilder) {
		this.appendCell(row, 0, stringBuilder);
		stringBuilder.append(" + ");
		this.appendCell(row, 1, stringBuilder);
		stringBuilder.append(" = ");
		this.appendCell(row, 2, stringBuilder);
		return stringBuilder;
	}

	/**
	 * Diese Methode fügt die Darstellung eine Zelle in den {@link StringBuilder} ein.
	 *
	 * @param row die Zeilennummer
	 * @param column die Spaltennummer
	 * @param stringBuilder der zu verwendende {@link StringBuilder}
	 * @return der verwendete {@link StringBuilder}
	 */
	private StringBuilder appendCell(
			final int row,
			final int column,
			@Nonnull final StringBuilder stringBuilder) {

		return this.appendCell(column, this.columns.get(column).get(row), stringBuilder);
	}

	/**
	 * Diese Methode fügt die Darstellung eine Zelle in den {@link StringBuilder} ein.
	 *
	 * @param column die Spaltennummer
	 * @param cell der Inhalt der Zelle
	 * @param stringBuilder der zu verwendende {@link StringBuilder}
	 * @return der verwendete {@link StringBuilder}
	 */
	private StringBuilder appendCell(
			final int column,
			@Nonnull final List<Symbol> cell,
			@Nonnull final StringBuilder stringBuilder) {

		this.addCellPadding(column, cell.size(), stringBuilder);
		new ReverseListIterator<>(cell).forEachRemaining(
				symbol -> stringBuilder.appendCodePoint(symbol.getIconCodePoint()));
		return stringBuilder;
	}

	/**
	 * Diese Methode fügt den Abstand für eine Zelle ein.
	 *
	 * @param column die Spaltennummer
	 * @param cellSize die Größe des Inhalts der Zelle
	 * @param stringBuilder der zu verwendende {@link StringBuilder}
	 * @return der verwendete {@link StringBuilder}
	 */
	private StringBuilder addCellPadding(
			final int column,
			final int cellSize,
			@Nonnull final StringBuilder stringBuilder) {

		this.addPadding(this.findColumnWidth(column) - cellSize, stringBuilder);
		return stringBuilder;
	}

	/**
	 * Diese Methode ermittelt die Breite einer Spalte.
	 *
	 * @param column die Spaltennummer
	 * @return die ermittelte Breite
	 */
	private int findColumnWidth(final int column) {
		return this.columns.get(column).stream()
				.mapToInt(List::size)
				.max()
				.orElse(0);
	}

	/**
	 * Diese Methode fügt den Abstand in den {@link StringBuilder} ein.
	 *
	 * @param padding die Anzahl der Zeichen für den Abstand
	 * @param stringBuilder der zu verwendende {@link StringBuilder}
	 * @return der verwendete {@link StringBuilder}
	 */
	private StringBuilder addPadding(
			final int padding,
			@Nonnull final StringBuilder stringBuilder) {

		IntStream.range(0, padding).forEach(i -> stringBuilder.append(' '));
		return stringBuilder;
	}
}
