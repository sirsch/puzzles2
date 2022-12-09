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
	 * Diese Methode gibt den ersten Summanden zurück.
	 *
	 * @return die Zelle des ersten Summanden
	 */
	@Nonnull
	public Cell getFirstSummand() {
		return this.firstSummand;
	}

	/**
	 * Diese Methode gibt den zweiten Summanden zurück.
	 *
	 * @return die Zelle des zweiten Summanden
	 */
	@Nonnull
	public Cell getSecondSummand() {
		return this.secondSummand;
	}

	/**
	 * Diese Methode gibt das Ergebnis zurück.
	 *
	 * @return die Zelle mit dem Ergebnis der Summe
	 */
	@Nonnull
	public Cell getSum() {
		return this.sum;
	}

	/**
	 * Diese Methode zeigt an, ob es bei der Auswertung der Summe zu einem Widerspruch kam.
	 *
	 * <p>
	 *     Falls die Summe wegen fehlender Werte noch nicht berechnet werden kann, liegt kein
	 *     Widerspruch vor.
	 * </p>
	 *
	 * @return {@code true} falls die Summe ausgewertet werden konnte und dabei ein Widerspruch
	 * festgestellt wurde, sonst {@code false}
	 */
	public boolean isContradiction() {
		return this.areAllValuesBound() && !this.evaluatePredicate();
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
	 * Diese Methode prüft, ob es sich bei den für die Symbole eingesetzen Werte um eine Lösung der
	 * Gleichung handelt.
	 *
	 * @return {@code true}, falls es sich um eine gültige Aussage handelt, sonst {@code false}
	 * @throws IllegalStateException wenn {@link #areAllValuesBound()} {@code == false}
	 */
	private boolean evaluatePredicate() {
		return this.firstSummand.calculateValue() + this.secondSummand.calculateValue()
				== this.sum.calculateValue();
	}
}
