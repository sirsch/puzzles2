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
	 * Dieses Feld enthält den {@link StringBuilder} zum Generieren der Zeilen.
	 */
	@Nonnull
	private final StringBuilder rowStringBuilder = new StringBuilder();

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
		this.out.println(this.generateRow(row));
	}

	/**
	 * Diese Methode erzeugt die Zeile.
	 *
	 * @param row die Nummer der Zeile
	 * @return die erzeugte Zeile
	 */
	private String generateRow(final int row) {
		this.rowStringBuilder.setLength(0);
		this.appendCell(row, 0);
		this.rowStringBuilder.append(" + ");
		this.appendCell(row, 1);
		this.rowStringBuilder.append(" = ");
		this.appendCell(row, 2);
		return this.rowStringBuilder.toString();
	}

	/**
	 * Diese Methode fügt die Darstellung eine Zelle in den {@link #rowStringBuilder} ein.
	 *
	 * @param row die Zeilennummer
	 * @param column die Spaltennummer
	 */
	private void appendCell(final int row, final int column) {
		this.appendCell(column, this.columns.get(column).get(row));
	}

	/**
	 * Diese Methode fügt die Darstellung eine Zelle in den {@link #rowStringBuilder} ein.
	 *
	 * @param column die Spaltennummer
	 * @param cell der Inhalt der Zelle
	 */
	private void appendCell(final int column, @Nonnull final List<Symbol> cell) {
		this.addCellPadding(column, cell.size());
		new ReverseListIterator<>(cell).forEachRemaining(
				symbol -> this.rowStringBuilder.appendCodePoint(symbol.getIconCodePoint()));
	}

	/**
	 * Diese Methode fügt den Abstand für eine Zelle ein.
	 *
	 * @param column die Spaltennummer
	 * @param cellSize die Größe des Inhalts der Zelle
	 */
	private void addCellPadding(final int column, final int cellSize) {
		this.addPadding(this.findColumnWidth(column) - cellSize);
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
	 * Diese Methode fügt den Abstand in den {@link #rowStringBuilder} ein.
	 *
	 * @param padding die Anzahl der Zeichen für den Abstand
	 */
	private void addPadding(final int padding) {
		IntStream.range(0, padding).forEach(ignore -> this.rowStringBuilder.append(' '));
	}
}
