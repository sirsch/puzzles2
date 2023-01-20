package software.sirsch.sa4e.puzzles;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Diese Klasse stellt Tests für {@link PuzzleSolverFactory} bereit.
 *
 * @author sirsch
 * @since 20.01.2023
 */
public class PuzzleSolverFactoryTest {

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleSolverFactory objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectUnderTest = PuzzleSolverFactory.getSingletonInstance();
	}

	/**
	 * Diese Methode prüft, dass {@link PuzzleSolverFactory#getSingletonInstance()} immer dieselbe
	 * Instanz zurückgibt.
	 */
	@Test
	public void testGetSingletonInstance() {
		assertSame(this.objectUnderTest, PuzzleSolverFactory.getSingletonInstance());
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverFactory#create()}.
	 */
	@Test
	public void testCreate() {
		PuzzleSolver firstResult;
		PuzzleSolver secondResult;

		firstResult = this.objectUnderTest.create();
		secondResult = this.objectUnderTest.create();

		assertNotNull(firstResult);
		assertNotNull(secondResult);
		assertNotSame(firstResult, secondResult);
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverFactory#forEach(Consumer)}.
	 */
	@Test
	public void testForEach() {
		Consumer<PuzzleSolver> acton = mock(Consumer.class);
		PuzzleSolver firstPuzzleSolver = this.objectUnderTest.create();
		PuzzleSolver secondPuzzleSolver = this.objectUnderTest.create();

		this.objectUnderTest.forEach(acton);

		verify(acton).accept(firstPuzzleSolver);
		verify(acton).accept(secondPuzzleSolver);
	}
}