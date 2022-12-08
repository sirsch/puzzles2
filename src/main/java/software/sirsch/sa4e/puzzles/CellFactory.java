package software.sirsch.sa4e.puzzles;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Diese Schnittstelle beschreibt die Fabrikmethode für {@link Cell}.
 *
 * @author sirsch
 * @since 08.12.2022
 */
@FunctionalInterface
public interface CellFactory {

	/**
	 * Diese Methode erzeugt eine {@link Cell} und legt die Zahl als Liste von Symbolen fest.
	 *
	 * @param row die zu setzende Spaltennummer
	 * @param column die zu setzende Zeilennummer
	 * @param symbols die zu setzende Zahl
	 * @return die erzeugte Instanz
	 */
	@Nonnull
	Cell create(int row, int column, @Nonnull List<Symbol> symbols);
}
