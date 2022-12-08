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
	 * Dieses Feld muss die Fabrik für {@link Puzzle} enthalten.
	 */
	@Nonnull
	private final PuzzleFactory puzzleFactory;

	/**
	 * Dieses Feld muss die Fabrik für {@link Addition} enthalten.
	 */
	@Nonnull
	private final AdditionFactory additionFactory;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public PuzzleBuilder() {
		this(Puzzle::new, Addition::new);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von Fabriken zu Testen.
	 *
	 * @param puzzleFactory die zu setzende {@link PuzzleFactory}
	 * @param additionFactory die zu setzende {@link AdditionFactory}
	 */
	protected PuzzleBuilder(
			@Nonnull final PuzzleFactory puzzleFactory,
			@Nonnull final AdditionFactory additionFactory) {

		this.puzzleFactory = puzzleFactory;
		this.additionFactory = additionFactory;
		this.rows = new Cell[ROW_COUNT][];

		for (int row = 0; row < ROW_COUNT; row++) {
			this.rows[row] = new Cell[COLUMN_COUNT];
		}
	}

	/**
	 * Diese Methode fügt eine Zelle hinzu.
	 *
	 * @param cell die hinzuzufügende Zelle
	 * @return diese Instanz für Invocation-Chaining
	 */
	public PuzzleBuilder withCell(@Nonnull final Cell cell) {
		this.addCell(cell.getRow(), cell.getColumn(), cell);
		return this;
	}

	/**
	 * Diese Methode fügt eine Zelle hinzu.
	 *
	 * @param column die Spaltennummer
	 * @param row die Zeilennummer
	 * @param cell die hinzuzufügende Zelle
	 */
	private void addCell(final int row, final int column, @Nonnull final Cell cell) {
		this.verify(row, column);
		this.verifySymbols(cell);
		this.rows[row][column] = cell;
	}

	/**
	 * Diese Methode prüft die Symbole der übergebenen Zelle.
	 *
	 * @param cell die Zelle, deren Symbole geprüft werden sollen
	 */
	private void verifySymbols(@Nonnull final Cell cell) {
		cell.getSymbols().forEach(this::verifySymbol);
	}

	/**
	 * Diese Methode prüft, ob es sich um ein für diese Instanz gültiges Symbol handelt.
	 *
	 * @param symbol das zu prüfende Symbol
	 */
	private void verifySymbol(@CheckForNull final Symbol symbol) {
		if (!this.symbols.contains(symbol)) {
			throw new IllegalArgumentException("Invalid Symbol detected! Symbols must be obtained "
					+ "by Method findOrCreateSymbol.");
		}
	}

	/**
	 * Diese Methode ermittelt eine Zelle anhand der Zeilen- und Spaltennummer.
	 *
	 * @param row die Zeilennummer der Zelle
	 * @param column die Spaltennummer der Zelle
	 * @return die ermittelte Zelle
	 * @throws IllegalStateException wenn die Zelle fehlt
	 */
	@Nonnull
	private Cell requireCell(final int row, final int column) {
		return this.findCell(row, column)
				.orElseThrow(() -> new IllegalStateException(
						"Cell at row " + row + " column " + column + " has not been provided!"));
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
			throw new IllegalStateException("Max symbol count (" + MAX_SYMBOLS + ") exceeded!");
		}

		this.symbols.add(symbol);
		return symbol;
	}

	/**
	 * Diese Methode erzeugt das Puzzle.
	 *
	 * @return die erzeugte Instanz.
	 */
	@Nonnull
	public Puzzle build() {
		return this.puzzleFactory.create(this.symbols, this.createAdditions());
	}

	/**
	 * Diese Methode erzeugt die Liste der Gleichungen.
	 *
	 * @return die erzeugte Liste
	 */
	@Nonnull
	private List<Addition> createAdditions() {
		return this.createAdditions(new ArrayList<>(ROW_COUNT +  COLUMN_COUNT));
	}

	/**
	 * Diese Methode sammelt die Gleichungen in einer Liste auf.
	 *
	 * @param listToFill die zu befüllende Liste
	 * @return die befüllte Liste
	 */
	@Nonnull
	private List<Addition> createAdditions(@Nonnull final List<Addition> listToFill) {
		for (int row = 0; row < ROW_COUNT; row++) {
			listToFill.add(this.additionFactory.create(
					this.requireCell(row, 0),
					this.requireCell(row, 1),
					this.requireCell(row, 2)));
		}

		for (int column = 0; column < COLUMN_COUNT; column++) {
			listToFill.add(this.additionFactory.create(
					this.requireCell(0, column),
					this.requireCell(1, column),
					this.requireCell(2, column)));
		}

		return listToFill;
	}

	/**
	 * Diese Methode ermittelt eine Zelle anhand der Zeilen- und Spaltennummer.
	 *
	 * @param row die Zeilennummer der Zelle
	 * @param column die Spaltennummer der Zelle
	 * @return die optional ermittelte Zelle
	 */
	@Nonnull
	private Optional<Cell> findCell(final int row, final int column) {
		this.verify(row, column);
		return Optional.ofNullable(this.rows[row][column]);
	}

	/**
	 * Diese Methode prüft, ob Zeilen- und Spaltennummern ok sind.
	 *
	 * @param row die zu prüfende Zeilennummer
	 * @param column die zu prüfende Spaltennummer
	 */
	private void verify(final int row, final int column) {
		if (row < 0 || row >= ROW_COUNT) {
			throw new IllegalArgumentException("Invalid row number!");
		}

		if (column < 0 || column >= COLUMN_COUNT) {
			throw new IllegalArgumentException("Invalid column number!");
		}
	}
}
