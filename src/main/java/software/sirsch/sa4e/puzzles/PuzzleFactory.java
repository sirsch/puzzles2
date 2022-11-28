package software.sirsch.sa4e.puzzles;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Diese Schnittstelle beschreibt die Fabrikmethode für {@link Puzzle}.
 *
 * @author sirsch
 * @since 28.11.2022
 */
@FunctionalInterface
public interface PuzzleFactory {

	/**
	 * Diese Methode erzeugt eine neue Instanz von {@link Puzzle}.
	 *
	 * @param symbols die zu verwendenden Symbole
	 * @param additions die zu verwendenden Gleichungen
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	Puzzle create(@Nonnull List<Symbol> symbols, @Nonnull List<Addition> additions);
}
