package software.sirsch.sa4e.puzzles;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt Tests für {@link CommonSolvePuzzleRequest} bereit.
 *
 * @author sirsch
 * @since 26.01.2023
 */
public class CommonSolvePuzzleRequestTest {

	/**
	 * Dieses Feld enthält eine Json zum Testen.
	 */
	private static final String JSON = "{\"server_id\":\"Steve Jobs\",\"raetsel_id\":42," +
			"\"row1\":[\"abc\",\"cdf\",\"eff\"],\"row2\":[\"ccc\",\"jjj\",\"abc\"]," +
			"\"row3\":[\"aca\",\"jac\",\"cba\"]}";

	/**
	 * Dieses Feld soll den {@link ObjectMapper} zum Testen enthalten.
	 */
	private ObjectMapper objectMapper;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Diese Methode prüft die Json-Deserialisierung.
	 *
	 * @throws JsonProcessingException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testJsonDeserialization() throws JsonProcessingException {
		CommonSolvePuzzleRequest result;

		result = this.objectMapper.readValue(JSON, CommonSolvePuzzleRequest.class);

		verifyResult(result);
	}

	/**
	 * Diese Methode prüft die Json-Serialisierung.
V	 */
	@Test
	public void testJsonSerialization() throws JsonProcessingException {
		CommonSolvePuzzleRequest result;

		result = this.objectMapper.readValue(
				this.objectMapper.writeValueAsString(
						this.objectMapper.readValue(JSON, CommonSolvePuzzleRequest.class)),
				CommonSolvePuzzleRequest.class);

		verifyResult(result);
	}

	/**
	 * Diese Methode prüft einen Request.
	 *
	 * @param request die zu prüfende Instanz
	 */
	private void verifyResult(CommonSolvePuzzleRequest request) {
		assertEquals("Steve Jobs", request.getServerId());
		assertEquals(42L, request.getRaetselId());
		assertEquals(List.of("abc", "cdf", "eff"), request.getRow1());
		assertEquals(List.of("ccc", "jjj", "abc"), request.getRow2());
		assertEquals(List.of("aca", "jac", "cba"), request.getRow3());
	}
}
