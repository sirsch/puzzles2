package software.sirsch.sa4e.puzzles;

import java.util.List;

import javax.annotation.Nonnull;

import static java.util.List.copyOf;

/**
 * Diese KLasse repräsentiert eine Zelle im Zahlenrätsel.
 *
 * @author sirsch
 * @since 21.11.2022
 */
public class Cell {

	/**
	 * Dieses Feld enthält die Zeilennummer.
	 */
	private final int row;

	/**
	 * Dieses Feld enthält die Spaltennummer.
	 */
	private final int column;

	/**
	 * Dieses Feld enthält die Zahl als Liste von Symbolen.
	 *
	 * <p>
	 *     Das Symbol mit Index 0 repräsentiert die Stelle mit dem kleinsten Stellenwert.
	 * </p>
	 */
	@Nonnull
	private final List<Symbol> symbols;

	/**
	 * Dieser Konstruktor legt die Zahl als Liste von Symbolen fest.
	 *
	 * @param row die zu setzende Spaltennummer
	 * @param column die zu setzende Zeilennummer
	 * @param symbols die zu setzende Zahl
	 */
	public Cell(final int row, final int column, @Nonnull final List<Symbol> symbols) {
		this.row = row;
		this.column = column;
		this.symbols = copyOf(symbols);
	}

	/**
	 * Diese Methode gibt die Zeilennummer zurück.
	 *
	 * @return die Zeilennummer
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * Diese Methode gibt die Spaltennummer zurück.
	 *
	 * @return die Spaltennummer
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * Diese Methode gibt die Symbole zurück.
	 *
	 * @return eine unveränderbare Liste der Symbole
	 */
	@Nonnull
	public List<Symbol> getSymbols() {
		return this.symbols;
	}

	/**
	 * Diese Methode prüft, ob alle Werte gebunden wurden.
	 *
	 * @return {@code true}, falls alle Werte gebunden sind, sonst {@code false}
	 */
	public boolean areAllValuesBound() {
		for (Symbol symbol : this.symbols) {
			if (!symbol.isValueBound()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Diese Methode berechnet den Wert der Zahl durch Ersetzung der Symbole durch ihren aktuell
	 * zugewiesenen Wert.
	 *
	 * @return die Zahl
	 * @throws IllegalStateException wenn {@link #areAllValuesBound()} {@code == false}
	 */
	public long calculateValue() {
		final int base = 10;
		long result = 0;
		long factor = 1;

		for (Symbol place : this.symbols) {
			result += factor * place.getBoundValue();
			factor *= base;
		}

		return result;
	}
}
