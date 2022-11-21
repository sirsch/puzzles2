package software.sirsch.sa4e.puzzles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;

/**
 * Diese Klasse repräsentiert ein Zahlenrätsel.
 *
 * @author sirsch
 * @since 21.11.2022
 */
public class PuzzleBuilder {

	/**
	 * Diese Konstante enthält die Anzahl der Zeilen.
	 */
	private static final int ROW_COUNT = 3;

	/**
	 * Diese Konstante enthält die Anzahl der Spalten.
	 */
	private static final int COLUMN_COUNT = 3;

	/**
	 * Diese Konstante enthält die maximale Anzahl verschiedener Symbole.
	 */
	private static final int MAX_SYMBOLS = 10;

	/**
	 * Dieses Feld enthält die Symbole in einer bestimmten Reihenfolge.
	 */
	@Nonnull
	private final List<Symbol> symbols = new ArrayList<>();

	/**
	 * Dieses Feld enthält das Gitter der Zellen als verschachteltes Array.
	 */
	@Nonnull
	private final Cell[][] rows;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public PuzzleBuilder() {
		this.rows = new Cell[ROW_COUNT][];

		for (int row = 0; row < ROW_COUNT; row++) {
			this.rows[row] = new Cell[COLUMN_COUNT];
		}
	}

	/**
	 * Diese Methode fügt eine Zelle hinzu.
	 *
	 * @param column die Spaltennummer
	 * @param row die Zeilennummer
	 * @param cell die hinzuzufügende Zelle
	 * @return diese Instanz für Invocation-Chaining
	 */
	public PuzzleBuilder withCell(final int column, final int row, @CheckForNull final Cell cell) {
		if (column < 0 || column >= COLUMN_COUNT) {
			throw new IllegalArgumentException("Invalid column number!");
		}

		if (row < 0 || row >= ROW_COUNT) {
			throw new IllegalArgumentException("Invalid row number!");
		}

		this.rows[row][column] = cell;
		return this;
	}

	/**
	 * Diese Methode verifiziert, dass alle Zellen belegt sind.
	 *
	 * @throws IllegalStateException wenn mindestens eine Zelle fehlt
	 */
	private void verifyAllCellsSet() {
		for (int row = 0; row < ROW_COUNT; row++) {
			for (int column = 0; column < COLUMN_COUNT; column++) {
				if (this.rows[row][column] == null) {
					throw new IllegalStateException(
							"Cell at row " + row + " column " + column + " has not been provided!");
				}
			}
		}
	}

	/**
	 * Diese Methode sucht oder erzeugt ein Symbol, falls es noch nicht vorhanden ist.
	 *
	 * @param symbolMessage das Protobuf-Symbol, dessen Eigenschaften übernommen werden sollen
	 * @return das gefundene oder erzeugte Symbol
	 */
	@Nonnull
	public Symbol findOrCreateSymbol(@Nonnull final Puzzles.Symbol symbolMessage) {
		return this.findOrCreateSymbol(symbolMessage.getId(), symbolMessage.getDescription());
	}

	/**
	 * Diese Methode sucht oder erzeugt ein Symbol, falls es noch nicht vorhanden ist.
	 *
	 * @param id die ID des zu suchenden Symbols
	 * @param description die Beschreibung für den Fall, dass ein neues Symbol erzeugt werden muss
	 * @return das gefundene oder erzeugte Symbol
	 */
	@Nonnull
	public Symbol findOrCreateSymbol(final int id, @CheckForNull final String description) {
		return this.findSymbol(id)
				.orElseGet(() -> this.createSymbol(id, description));
	}

	/**
	 * Diese Methode sucht ein Symbol anhand seiner ID.
	 *
	 * @param id die ID des zu suchenden Symbols
	 * @return das optional ermittelte Symbol
	 */
	@Nonnull
	private Optional<Symbol> findSymbol(final int id) {
		return this.symbols.stream()
				.filter(symbol -> symbol.getId() == id)
				.findFirst();
	}

	/**
	 * Diese Methode erzeugt und registriert ein neues Symbol.
	 *
	 * @param id die ID des zu erzeugenden Symbols
	 * @param description die Beschreibung des zu erzeugenden Symbols
	 * @return das erzeugte Symbol
	 */
	@Nonnull
	private Symbol createSymbol(final int id, @CheckForNull final String description) {
		return this.addSymbol(new Symbol(id, description));
	}

	/**
	 * Diese Methode registriert ein Symbol.
	 *
	 * @param symbol das hinzuzufügende Symbol
	 * @return das hinzugefügte Symbol
	 */
	@Nonnull
	private Symbol addSymbol(@Nonnull final Symbol symbol) {
		if (this.symbols.size() >= MAX_SYMBOLS) {
			throw new IllegalArgumentException("Max symbol count (" + MAX_SYMBOLS + ") exceeded!");
		}

		this.symbols.add(symbol);
		return symbol;
	}
}
