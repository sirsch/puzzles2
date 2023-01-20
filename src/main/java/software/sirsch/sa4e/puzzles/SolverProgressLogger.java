package software.sirsch.sa4e.puzzles;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Diese Schnittstelle beschreibt die Methoden zur Übergabe von Statusinformationen beim Lösen
 * eines Rätsels.
 *
 * @author sirsch
 * @since 20.01.2023
 */
public interface SolverProgressLogger {

	/**
	 * Diese Methode zeichnet auf, ob eine Permutation eine Lösung des Rätsels darstellt.
	 *
	 * @param permutation die zu betrachtete Permutation
	 * @param isSolution {@code true}, falls es sich um eine Lösung handelt, sonst {@code false}
	 */
	default void logPermutation(
			@Nonnull final List<Symbol> permutation,
			final boolean isSolution) {

		StringBuilder stringBuilder = new StringBuilder("Permutation ");

		permutation.forEach(symbol -> stringBuilder
				.appendCodePoint(symbol.getIconCodePoint())
				.append(':')
				.append(symbol.getBoundValue())
				.append(' '));

		stringBuilder.append("is ");

		if (!isSolution) {
			stringBuilder.append("not ");
		}

		stringBuilder.append("a solution.");
		this.log(stringBuilder.toString());
	}

	/**
	 * Diese Methode zeichnet eine Nachricht auf.
	 *
	 * @param message die Nachricht
	 */
	void log(@Nonnull String message);
}
