package software.sirsch.sa4e.puzzles;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

/**
 * Diese Klasse gibt zufällig Icon-Code-Points zurück, ohne dabei einen doppelt zurückzugeben.
 *
 * @author sirsch
 * @since 17.12.2022
 */
public class RandomIconGenerator {

	/**
	 * Dieses Feld enthält die Vorgabe-Icons.
	 */
//	protected static final List<Integer> DEFAULT_ICON_CODE_POINTS = List.of(
//			0x1F408,
//			0x1F436,
//			0x1F412,
//			0x1F434,
//			0x1F411,
//			0x1F42A,
//			0x1F418,
//			0x1F401,
//			0x1F427,
//			0x1F425,
//			0x1F41F);
	protected static final List<Integer> DEFAULT_ICON_CODE_POINTS = IntStream.range('A', 'Z')
			.boxed()
			.collect(Collectors.toList());

	/**
	 * Dieses Feld muss die Liste der übrigen Icons enthalten.
	 */
	@Nonnull
	private final List<Integer> iconCodePoints;

	/**
	 * Dieses Feld muss den Pseudozufallszahlengenerator enthalten.
	 */
	@Nonnull
	private final Random random;

	/**
	 * Dieser Konstruktor führt die Initialisierung mit Vorgabewerten durch.
	 */
	public RandomIconGenerator() {
		this(DEFAULT_ICON_CODE_POINTS, new Random());
	}

	/**
	 * Dieser Konstruktor setzt die Liste der Icon-Code-Points und den Pseudozufallszahlengenerator.
	 *
	 * @param iconCodePoints die zu setzende Liste
	 * @param random der zu setzende Generator
	 */
	public RandomIconGenerator(
			@Nonnull final List<Integer> iconCodePoints,
			@Nonnull final Random random) {

		this.iconCodePoints = new ArrayList<>(iconCodePoints);
		this.random = random;
	}

	/**
	 * Diese Methode wählt zufällig das nächste Icon.
	 *
	 * @return der Code-Point des nächsten Icons
	 */
	public int nextIconCodePoint() {
		if (this.iconCodePoints.isEmpty()) {
			throw new NoSuchElementException("No icons left!");
		}

		return this.iconCodePoints.remove(this.random.nextInt(this.iconCodePoints.size()));
	}
}
