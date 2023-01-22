package software.sirsch.sa4e.puzzles;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link SolverProgressLogger} bereit.
 *
 * @author sirsch
 * @since 20.01.2023
 */
public class SolverProgressLoggerTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private SolverProgressLogger objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = spy(SolverProgressLogger.class);
	}

	/**
	 * Diese Methode prüft {@link SolverProgressLogger#logPermutation(List, boolean)}.
	 */
	@Test
	public void testLogPermutationSolution() {
		Symbol firstSymbol = mock(Symbol.class);
		Symbol secondSymbol = mock(Symbol.class);

		when(firstSymbol.getIconCodePoint()).thenReturn((int) 'A');
		when(firstSymbol.getBoundValue()).thenReturn((byte) 1);
		when(secondSymbol.getIconCodePoint()).thenReturn((int) 'B');
		when(secondSymbol.getBoundValue()).thenReturn((byte) 2);

		this.objectUnderTest.logPermutation(List.of(firstSymbol, secondSymbol), true);

		verify(this.objectUnderTest).log("Permutation A:1 B:2 is a solution.");
	}

	/**
	 * Diese Methode prüft {@link SolverProgressLogger#logPermutation(List, boolean)}.
	 */
	@Test
	public void testLogPermutationNotSolution() {
		Symbol firstSymbol = mock(Symbol.class);
		Symbol secondSymbol = mock(Symbol.class);

		when(firstSymbol.getIconCodePoint()).thenReturn((int) 'A');
		when(firstSymbol.getBoundValue()).thenReturn((byte) 1);
		when(secondSymbol.getIconCodePoint()).thenReturn((int) 'B');
		when(secondSymbol.getBoundValue()).thenReturn((byte) 2);

		this.objectUnderTest.logPermutation(List.of(firstSymbol, secondSymbol), false);

		verify(this.objectUnderTest).log("Permutation A:1 B:2 is not a solution.");
	}

	/**
	 * Diese Methode prüft {@link SolverProgressLogger#close()}.
	 */
	@Test
	public void testClose() {
		assertDoesNotThrow(() -> this.objectUnderTest.close());
	}
}
