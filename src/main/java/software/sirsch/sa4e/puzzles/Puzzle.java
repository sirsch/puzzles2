package software.sirsch.sa4e.puzzles;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import static java.util.List.copyOf;

/**
 * Diese Klasse bildet das Zahlenrätsel ab.
 *
 * <p>
 *     Designentscheidung: Diese Klasse ist nicht thread-safe!
 * </p>
 *
 * @author sirsch
 * @since 26.11.2022
 */
public class Puzzle {

	/**
	 * Dieses Feld muss die Liste der Symbole enthalten.
	 */
	@Nonnull
	private final List<Symbol> symbols;

	/**
	 * Dieses Feld muss die Liste der Additionen, aus denen das Rätsel besteht, enthalten.
	 */
	@Nonnull
	private final List<Addition> additions;

	/**
	 * Dieser Konstruktor legt die Symbole und Additionen fest.
	 *
	 * @param symbols die zu setzende Liste der Symbole
	 * @param additions die zu setzende Liste der Additionen
	 * @see PuzzleBuilder für die Erzeugung von Instanzen
	 */
	protected Puzzle(final @Nonnull List<Symbol> symbols, final @Nonnull List<Addition> additions) {
		this.symbols = copyOf(symbols);
		this.additions = copyOf(additions);
	}

	/**
	 * Diese Methode gibt die Liste der Symbole zurück.
	 *
	 * @return die Symbole als unveränderliche Liste
	 */
	@Nonnull
	public List<Symbol> getSymbols() {
		return this.symbols;
	}

	/**
	 * Diese Methode prüft, ob es sich bei der übergebenen Belegung von Ziffern um eine Lösung
	 * des Zahlenrätsels handelt.
	 *
	 * <p>
	 *     Damit es sich um eine Lösung handelt, müssen alle Symbole mit Werten belegt worden sein
	 *     und diese Belegung darf in den Gleichungen nicht zu einem Widerspruch führen.
	 * </p>
	 *
	 * @param values die zu prüfenden Werte in der Reihenfolge von {@link #getSymbols()}
	 * @return {@code true} falls es sich um eine Lösung des Rätsels handelt, sonst {@code false}
	 */
	public boolean isSolution(@Nonnull final List<Byte> values) {
		return values.size() >= this.symbols.size() && !this.isContradiction(values);
	}


	/**
	 * Diese Methode prüft, ob die übergebene Belegung der Symbole im Widerspruch zu den Gleichungen
	 * des Rätsels steht.
	 *
	 * <p>
	 *     Dabei müssen nicht allen Symbolen ein Wert zugewiesen werden. Bei der Übergabe sind
	 *     null-Elemente erlaubt, die anzeigen, dass dem entsprechenden Symbol kein Wert zugewiesen
	 *     ist.
	 * </p>
	 *
	 * @param values die Werte der Symbole in der Reihenfolge von {@link #getSymbols()}
	 * @return {@code true} falls mindestens eine Gleichung ausgewertet werden konnte und dabei ein
	 * Widerspruch festgestellt wurde, sonst {@code false}
	 */
	public boolean isContradiction(@Nonnull final List<Byte> values) {
		this.assignValues(values);
		return this.isContradiction();
	}

	/**
	 * Diese Methode weist den Symbolen die übergebenen Werte zu.
	 *
	 * <p>
	 *     Dabei müssen nicht allen Symbolen ein Wert zugewiesen werden. Bei der Übergabe sind
	 *     null-Elemente erlaubt, die anzeigen, dass dem entsprechenden Symbol kein Wert zugewiesen
	 *     ist.
	 * </p>
	 *
	 * @param value die Werte der Symbole in der Reihenfolge von {@link #getSymbols()}
	 */
	private void assignValues(@Nonnull final List<Byte> value) {
		this.assignValues(new FillWithNullIterator<>(value.iterator()));
	}

	/**
	 * Diese Methode weist den Symbolen die übergebenen Werte zu.
	 *
	 * <p>
	 *     Dabei muss der Iterator für alle Symbole ein Element bereitstellen. Da nicht alle Symbole
	 *     mit einem Wert belegt werden müssen, darf der Iterator null-Elemente zurückgeben, die
	 *     anzeigen, dass dem entsprechenden Symbol kein Wert zugewiesen ist.
	 * </p>
	 *
	 * @param value ein Iterator über die Werte der Symbole in der Reihenfolge von
	 * {@link #getSymbols()}
	 */
	private void assignValues(@Nonnull final Iterator<Byte> value) {
		for (Symbol symbol : this.symbols) {
			symbol.bindValue(value.next());
		}
	}

	/**
	 * Diese Methode zeigt an, ob es bei der Auswertung der Gleichungen zu einem Widerspruch kam.
	 *
	 * <p>
	 *     Falls eine Summe wegen fehlender Werte noch nicht berechnet werden kann, liegt kein
	 *     Widerspruch vor.
	 * </p>
	 *
	 * @return {@code true} falls mindestens eine Gleichung ausgewertet werden konnte und dabei ein
	 * Widerspruch festgestellt wurde, sonst {@code false}
	 */
	private boolean isContradiction() {
		for (Addition addition : this.additions) {
			if (addition.isContradiction()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Diese Methode ermittelt die Zellen des Puzzles.
	 *
	 * @return die Menge der Zellen
	 */
	@Nonnull
	public Set<Cell> getCells() {
		Set<Cell> result = new HashSet<>();

		for (Addition addition : this.additions) {
			result.add(addition.getFirstSummand());
			result.add(addition.getSecondSummand());
			result.add(addition.getSum());
		}

		return result;
	}

	/**
	 * Diese Klasse stellt einen Dekorateur für Iterator bereit, der hinter den Werten des
	 * ursprünglichen Iterators unendlich viele null-Elemente zurückgeben kann.
	 *
	 * @param <E> der Elementtyp des Iterators
	 */
	protected static class FillWithNullIterator<E> implements Iterator<E> {

		/**
		 * Dieses Feld muss den zu dekorierenden Iterator enthalten.
		 */
		@Nonnull
		private final Iterator<E> wrappedIterator;

		/**
		 * Dieser Konstruktor legt den zu dekorierenden Iterator fest.
		 *
		 * @param wrappedIterator die zu übernehmende Instanz
		 */
		protected FillWithNullIterator(@Nonnull final Iterator<E> wrappedIterator) {
			this.wrappedIterator = wrappedIterator;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public E next() {
			if (this.wrappedIterator.hasNext()) {
				return this.wrappedIterator.next();
			} else {
				return null;
			}
		}
	}
}
