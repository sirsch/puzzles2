package software.sirsch.sa4e.puzzles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import static com.google.common.math.IntMath.pow;

/**
 * Diese Klasse stellt den Generator für Zahlenrätsel bereit.
 *
 * @author sirsch
 * @since 18.11.2022
 */
public class PuzzleGenerator {

	/**
	 * Dieses Feld enthält die nächste zu vergebende ID für Symbole.
	 */
	private int nextID = 1;

	/**
	 * Dieses Feld enthält eine Zuordnung vom Ziffernzeichen auf das {@link Symbol}.
	 */
	private final Map<Integer, Symbol> symbolLookup = new HashMap<>();

	/**
	 * Dieses Feld muss den Pseudozufallszahlengenerator enthalten.
	 */
	@Nonnull
	private final Random random;

	/**
	 * Dieses Feld muss den {@link PuzzleBuilder} enthalten.
	 */
	@Nonnull
	private final PuzzleBuilder puzzleBuilder;

	/**
	 * Dieses Feld muss die {@link CellFactory} enthalten.
	 */
	@Nonnull
	private final CellFactory cellFactory;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public PuzzleGenerator() {
		this(new Random(), new PuzzleBuilder(), Cell::new);
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von Instanzen zum Testen.
	 *
	 * @param random der zu setzende Pseudozufallszahlengenerator
	 * @param puzzleBuilder der zu setzende {@link PuzzleBuilder}
	 * @param cellFactory die zu setzende {@link CellFactory}
	 */
	protected PuzzleGenerator(
			@Nonnull final Random random,
			@Nonnull final PuzzleBuilder puzzleBuilder,
			@Nonnull final CellFactory cellFactory) {

		this.random = random;
		this.puzzleBuilder = puzzleBuilder;
		this.cellFactory = cellFactory;
	}

	/**
	 * Diese Methode erzeugt ein neues Zahlenrätsel.
	 *
	 * @param maxNumberOfDigits die maximale Stellenzahl für die Summanden
	 * @return das erzeugte Puzzle
	 */
	@Nonnull
	public Puzzle generate(final int maxNumberOfDigits) {
		return this.generate(
				this.generateRandomInteger(maxNumberOfDigits),
				this.generateRandomInteger(maxNumberOfDigits),
				this.generateRandomInteger(maxNumberOfDigits),
				this.generateRandomInteger(maxNumberOfDigits));
	}

	/**
	 * Diese Methode erzeugt eine Zufallszahl mit einer vorgegebenen Anzahl von Stellen.
	 *
	 * @param maxNumberOfDigits die maximale Anzahl von Stellen
	 * @return die erzeugte Zufallszahl
	 */
	private int generateRandomInteger(final int maxNumberOfDigits) {
		final int base = 10;

		return this.random.nextInt(pow(base, maxNumberOfDigits));
	}

	/**
	 * Diese Methode erzeugt ein neues Zahlenrätsel.
	 *
	 * @param cell00 der Wert für die Zelle in Zeile 0 Spalte 0
	 * @param cell01 der Wert für die Zelle in Zeile 0 Spalte 1
	 * @param cell10 der Wert für die Zelle in Zeile 1 Spalte 0
	 * @param cell11 der Wert für die Zelle in Zeile 1 Spalte 1
	 * @return das erzeugte Puzzle
	 */
	@Nonnull
	protected Puzzle generate(
			final int cell00,
			final int cell01,
			final int cell10,
			final int cell11) {

		this.addCells(cell00, cell01, cell10, cell11);
		return this.puzzleBuilder.build();
	}

	/**
	 * Diese Methode erzeugt die Zellen für das Zahlenrätsel und fügt diese dem
	 * {@link #puzzleBuilder} hinzu.
	 *
	 * @param cell00 der Wert für die Zelle in Zeile 0 Spalte 0
	 * @param cell01 der Wert für die Zelle in Zeile 0 Spalte 1
	 * @param cell10 der Wert für die Zelle in Zeile 1 Spalte 0
	 * @param cell11 der Wert für die Zelle in Zeile 1 Spalte 1
	 */
	private void addCells(
			final int cell00,
			final int cell01,
			final int cell10,
			final int cell11) {

		this.addCell(0, 0, cell00);
		this.addCell(0, 1, cell01);
		this.addCell(0, 2, cell00 + cell01);
		this.addCell(1, 0, cell10);
		this.addCell(1, 1, cell11);
		this.addCell(1, 2, cell10 + cell11);
		this.addCell(2, 0, cell00 + cell10);
		this.addCell(2, 1, cell01 + cell11);
		this.addCell(2, 2, cell00 + cell01 + cell10 + cell11);
	}

	/**
	 * Diese Methode erzeugt eine Zelle und fügt diese dem {@link #puzzleBuilder} hinzu.
	 *
	 * @param row die Zeilennummer
	 * @param column die Spaltennummer
	 * @param value der Wert
	 */
	@Nonnull
	private void addCell(final int row, final int column, final int value) {
		this.puzzleBuilder.withCell(this.cellFactory.create(row, column, this.encodeValue(value)));
	}

	/**
	 * Diese Methode erzeugt kodiert einen Zahlenwert als Liste von Symbolen.
	 *
	 * <p>
	 *     Dabei ist das erste Symbol der Liste das niederwertigste.
	 * </p>
	 *
	 * @param value der zu kodierende Wert
	 * @return der als Symbolliste kodierte Wert
	 */
	@Nonnull
	private List<Symbol> encodeValue(final int value) {
		return new StringBuilder()
				.append(value)
				.reverse()
				.chars()
				.mapToObj(this::findSymbol)
				.collect(Collectors.toList());
	}

	/**
	 * Diese Methode sucht das Symbol für eine Ziffer heraus.
	 *
	 * <p>
	 *     Falls ein Symbol noch nicht vorhanden ist, wird es erzeugt.
	 * </p>
	 *
	 * @param digitChar der Zeichenwert der Ziffer (char)
	 * @return das ermittelte Symbol
	 */
	@Nonnull
	private Symbol findSymbol(final int digitChar) {
		return this.symbolLookup.computeIfAbsent(digitChar, ignore -> this.createSymbol());
	}

	/**
	 * Diese Methode erzeugt ein neues Symbol.
	 *
	 * @return das erzeugte Symbol
	 */
	@Nonnull
	private Symbol createSymbol() {
		return this.puzzleBuilder.findOrCreateSymbol(this.nextID++, null);
	}
}
