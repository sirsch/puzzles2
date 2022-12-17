package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Diese Klasse stellt Tests für {@link RandomIconGenerator} bereit.
 *
 * @author sirsch
 * @since 17.12.2022
 */
public class RandomIconGeneratorTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private RandomIconGenerator objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = new RandomIconGenerator();
	}

	/**
	 * Diese Methode prüft {@link RandomIconGenerator#nextIconCodePoint()}.
	 */
	@Test
	public void testNextIconCodePoint() {
		List<Integer> results;

		results = IntStream.range(0, RandomIconGenerator.DEFAULT_ICON_CODE_POINTS.size())
				.mapToObj(i -> this.objectUnderTest.nextIconCodePoint())
				.collect(Collectors.toList());

		assertThat(
				results,
				containsInAnyOrder(RandomIconGenerator.DEFAULT_ICON_CODE_POINTS.toArray(
						new Integer[RandomIconGenerator.DEFAULT_ICON_CODE_POINTS.size()])));
		results.forEach(codePoint -> System.out.print(Character.toString(codePoint)));
		System.out.println();
	}

	/**
	 * Diese Methode prüft {@link RandomIconGenerator#nextIconCodePoint()}.
	 */
	@Test
	public void testNextIconCodePointToManyInvocations() {
		IntStream.range(0, RandomIconGenerator.DEFAULT_ICON_CODE_POINTS.size())
				.forEach(i -> this.objectUnderTest.nextIconCodePoint());

		assertThrows(NoSuchElementException.class, () -> this.objectUnderTest.nextIconCodePoint());
	}
}