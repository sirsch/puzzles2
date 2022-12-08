package software.sirsch.sa4e.puzzles;

import java.util.List;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.Symbol;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse dokumentiert Annahmen bezüglich Protocol Buffers in Java in Form von Tests.
 *
 * @author sirsch
 * @since 04.12.2022
 */
public class ProtobufAssertions {

	/**
	 * Diese Methode verdeutlicht, dass die Listen von "repeated" Feldern nicht null sind.
	 */
	@Test
	public void testListCannotBeNull() {
		SolvePuzzleRequest objectUnderTest;
		List<Symbol> result;

		objectUnderTest = SolvePuzzleRequest.newBuilder().build();

		result = objectUnderTest.getSymbolsList();

		assertNotNull(result);
		assertEquals(emptyList(), result);
	}
}
