package software.sirsch.sa4e.puzzles;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Diese Klasse stellt Tests für {@link Injector} bereit.
 *
 * @author sirsch
 * @since 22.01.2023
 */
public class InjectorTest {

	/**
	 * Dieses Feld muss das Test-Ziel enthalten.
	 */
	private TestTarget testTarget;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private Injector objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.testTarget = new TestTarget();

		this.objectUnderTest = new Injector();
	}

	/**
	 * Diese Methode prüft {@link Injector#apply(Object)}.
	 */
	@Test
	public void testApply() {
		Consumer<TestTarget> result;

		this.testTarget.firstString = "firstString";

		result = this.objectUnderTest.apply("injectedString");

		assertNotNull(result);
		result.accept(new TestTarget());
		result.accept(this.testTarget);
		assertEquals("injectedString", this.testTarget.firstString);
		assertEquals("injectedString", this.testTarget.secondString);
		assertNull(this.testTarget.notInjectableString);
		assertNull(this.testTarget.integer);
	}

	/**
	 * Diese Klasse stellt Objekte für den Test bereit.
	 */
	private static class TestTarget {

		@Injectable
		private String firstString;

		@Injectable
		private String secondString;

		private String notInjectableString;

		@Injectable
		private Integer integer;
	}
}
