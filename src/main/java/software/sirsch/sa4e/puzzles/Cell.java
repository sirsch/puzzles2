package software.sirsch.sa4e.puzzles;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Diese KLasse repräsentiert eine Zelle im Zahlenrätsel.
 *
 * @author sirsch
 * @since 21.11.2022
 */
public class Cell {

	/**
	 * Dieses Feld enthält die Zahl als Liste von Symbolen.
	 *
	 * <p>
	 *     Das Symbol mit Index 0 repräsentiert die Stelle mit dem kleinsten Stellenwert.
	 * </p>
	 */
	@Nonnull
	private final List<Symbol> number;

	/**
	 * Dieser Konstruktor legt die Zahl als Liste von Symbolen fest.
	 *
	 * @param number die zu setzende Zahl
	 */
	public Cell(@Nonnull final List<Symbol> number) {
		this.number = number;
	}

	/**
	 * Diese Methode prüft, ob alle Werte gebunden wurden.
	 *
	 * @return {@code true}, falls alle Werte gebunden sind, sonst {@code false}
	 */
	public boolean areAllValuesBound() {
		for (Symbol symbol : this.number) {
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

		for (Symbol place : this.number) {
			result += factor * place.getBoundValue();
			factor *= base;
		}

		return result;
	}
}
