package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

/**
 * Diese Schnittstelle beschreibt die Fabrikmethode für {@link Addition}.
 *
 * @author sirsch
 * @since 23.11.2022
 */
@FunctionalInterface
public interface AdditionFactory {

	/**
	 * Diese Methode erzeugt eine neue Instanz von {@link Addition}.
	 *
	 * @param firstSummand die Zelle des ersten Summanden
	 * @param secondSummand die Zelle des zweiten Summanden
	 * @param sum die Zelle mit dem Ergebnis der Summe
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	Addition create(@Nonnull Cell firstSummand, @Nonnull Cell secondSummand, @Nonnull Cell sum);
}
