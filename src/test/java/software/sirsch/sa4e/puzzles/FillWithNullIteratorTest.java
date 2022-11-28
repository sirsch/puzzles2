package software.sirsch.sa4e.puzzles;

import java.util.Iterator;

import software.sirsch.sa4e.puzzles.Puzzle.FillWithNullIterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link FillWithNullIteratorTest} bereit.
 *
 * @author sirsch
 * @since 27.11.2022
 */
public class FillWithNullIteratorTest {

	/**
	 * Dieses Feld soll den Mock des zu dekorierenden Iterators enthalten.
	 */
	private Iterator<String> wrappedIterator;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private FillWithNullIterator<String> objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.wrappedIterator = mock(Iterator.class);

		this.objectUnderTest = new FillWithNullIterator(this.wrappedIterator);
	}

	/**
	 * Diese Methode prüft, dass {@link FillWithNullIterator#hasNext()} {@code true} zurückgibt,
	 * auch wenn der dekorierte Iterator erschöpft ist.
	 */
	@Test
	public void testHasNext() {
		boolean result;

		when(this.wrappedIterator.hasNext()).thenReturn(false);

		result = this.objectUnderTest.hasNext();

		assertTrue(result);
	}

	/**
	 * Diese Methode prüft, dass {@link FillWithNullIterator#next()} einen Wert des dekorierten
	 * Iterators zurückgibt, solange dieser nicht erschöpft ist.
	 */
	@Test
	public void testNextValueFromWrappedIterator() {
		String result;

		when(this.wrappedIterator.hasNext()).thenReturn(true);
		when(this.wrappedIterator.next()).thenReturn("nextValue");

		result = this.objectUnderTest.next();

		assertEquals("nextValue", result);
	}

	/**
	 * Diese Methode prüft, dass {@link FillWithNullIterator#next()} {@code null} zurückgibt, wenn
	 * der dekorierte Iterator erschöpft ist.
	 */
	@Test
	public void testNextNull() {
		String result;

		when(this.wrappedIterator.hasNext()).thenReturn(false);

		result = this.objectUnderTest.next();

		assertNull(result);
	}
}
