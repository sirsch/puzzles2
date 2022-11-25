package software.sirsch.sa4e.puzzles;

import javax.annotation.CheckForNull;

/**
 * Diese Klasse repräsentiert ein Symbol eines Zahlenrätsels.
 *
 * @author sirsch
 * @since 21.11.2022
 */
public class Symbol {

	/**
	 * Dieser Wert zeigt an, dass kein Wert gebunden wurde.
	 */
	private static final byte VALUE_NOT_BOUND = -1;

	/**
	 * Diese Konstante enthält den maximalen zulässigen Wert.
	 */
	private static final int MAX_VALUE = 9;

	/**
	 * Dieses Feld enthält die ID des Symbols.
	 */
	private final int id;

	/**
	 * Dieses kann die Beschreibung enthalten.
	 */
	@CheckForNull
	private final String description;

	/**
	 * Dieses Feld enthält den gebundenen Wert für das Symbol oder {@link #VALUE_NOT_BOUND}, falls
	 * kein Wert gebunden wurde.
	 */
	private byte boundValue = VALUE_NOT_BOUND;

	/**
	 * Dieser Konstruktor erzeugt ein neues Symbol mit ID und Beschreibung.
	 * <p>
	 * Dieser Konstruktor wird typischerweise vom {@link PuzzleBuilder} aufgerufen.
	 *
	 * @param id die zu setzende ID
	 * @param description die zu setzende Beschreibung
	 */
	protected Symbol(final int id, @CheckForNull final String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * Diese Methode bindet einen Wert.
	 *
	 * @param value der zu bindende Wert oder {@code null}, falls der Wert ungebunden sein soll
	 */
	public void bindValue(@CheckForNull final Byte value) {
		if (value == null) {
			this.boundValue = VALUE_NOT_BOUND;
		} else if (value < 0 || value > MAX_VALUE) {
			throw new IllegalArgumentException("Value " + value + " is not a valid digit!");
		} else {
			this.boundValue = value;
		}
	}

	/**
	 * Diese Methode prüft, ob ein Wert gebunden wurde.
	 *
	 * @return {@code true}, falls der Wert gebunden ist, sonst {@code false}
	 */
	public boolean isValueBound() {
		return this.boundValue != VALUE_NOT_BOUND;
	}

	/**
	 * Diese Methode gibt den gebundenen Wert zurück.
	 *
	 * @return der Wert
	 * @throws IllegalStateException wenn {@link #isValueBound()} {@code == false}
	 */
	public byte getBoundValue() {
		if (this.boundValue == VALUE_NOT_BOUND) {
			throw new IllegalStateException("Value not bound!");
		}

		return this.boundValue;
	}

	/**
	 * Diese Methode gibt die ID zurück.
	 *
	 * @return die ID
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Diese Methode gibt die Beschreibung zurück.
	 *
	 * @return die Beschreibung, falls vorhanden
	 */
	@CheckForNull
	public String getDescription() {
		return this.description;
	}
}
