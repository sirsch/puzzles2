package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Klasse beschreibt eine Addition aus dem Zahlenrätsel.
 *
 * @author sirsch
 * @since 25.11.2022
 */
public class Addition {

	/**
	 * Dieses Feld soll die Zelle enthalten, die den ersten Summanden repräsentiert.
	 */
	@Nonnull
	private final Cell firstSummand;

	/**
	 * Dieses Feld soll die Zelle enthalten, die den zweiten Summanden repräsentiert.
	 */
	@Nonnull
	private final Cell secondSummand;

	/**
	 * Dieses Feld soll die Zelle enthalten, die das Ergebnis der Summe repräsentiert.
	 */
	@Nonnull
	private final Cell sum;

	/**
	 * Dieser Konstruktor legt die Zellen der beiden Summanden und des Ergebnisses fest.
	 *
	 * @param firstSummand die Zelle des ersten Summanden
	 * @param secondSummand die Zelle des zweiten Summanden
	 * @param sum die Zelle mit dem Ergebnis der Summe
	 */
	public Addition(
			@Nonnull final Cell firstSummand,
			@Nonnull final Cell secondSummand,
			@Nonnull final Cell sum) {

		this.firstSummand = firstSummand;
		this.secondSummand = secondSummand;
		this.sum = sum;
	}

	/**
	 * Diese Methode zeigt an, ob es bei der Auswertung der Summe zu einem Widerspruch kam.
	 *
	 * <p>
	 *     Falls die Summe wegen fehlender Werte noch nicht berechnet werden kann, liegt noch kein
	 *     Widerspruch vor.
	 * </p>
	 *
	 * @return {@code false} falls die Summe ausgewertet werden konnte und dabei ein Widerspruch
	 * festgestellt wurde, sonst {@code true}
	 * @see org.apache.commons.lang3.BooleanUtils#isFalse(Boolean)
	 */
	public boolean isFalse() {
		return this.areAllValuesBound() && !this.evaluate();
	}

	/**
	 * Diese Methode prüft, ob alle Werte gebunden wurden.
	 *
	 * @return {@code true}, falls alle Werte gebunden sind, sonst {@code false}
	 */
	private boolean areAllValuesBound() {
		return this.firstSummand.areAllValuesBound()
				&& this.secondSummand.areAllValuesBound()
				&& this.sum.areAllValuesBound();
	}

	/**
	 * Diese Methode prüft, ob die Summe stimmt.
	 *
	 * @return {@code true}, falls es sich um eine gültige Aussage handelt, sonst {@code false}
	 * @throws IllegalStateException wenn {@link #areAllValuesBound()} {@code == false}
	 */
	private boolean evaluate() {
		return this.firstSummand.calculateValue() + this.secondSummand.calculateValue()
				== this.sum.calculateValue();
	}
}
