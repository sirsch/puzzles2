package software.sirsch.sa4e.puzzles;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
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
public class CommonSolvePuzzleResponseTest {

	/**
	 * Dieses Feld enthält eine Json zum Testen.
	 */
	private static final String JSON = "{\"server_id\":\"Elon Musk\",\"raetsel_id\":42," +
			"\"row1\":[123,456,789],\"row2\":[444,333,222],\"row3\":[023,211,763],\"time\":2.3444}";

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
		this.objectMapper.enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature());
	}

	/**
	 * Diese Methode prüft die Json-Deserialisierung.
	 *
	 * @throws JsonProcessingException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testJsonDeserialization() throws JsonProcessingException {
		CommonSolvePuzzleResponse result;

		result = this.objectMapper.readValue(JSON, CommonSolvePuzzleResponse.class);

		verifyResult(result);
	}

	/**
	 * Diese Methode prüft die Json-Serialisierung.
V	 */
	@Test
	public void testJsonSerialization() throws JsonProcessingException {
		CommonSolvePuzzleResponse result;

		result = this.objectMapper.readValue(
				this.objectMapper.writeValueAsString(
						this.objectMapper.readValue(JSON, CommonSolvePuzzleResponse.class)),
				CommonSolvePuzzleResponse.class);

		verifyResult(result);
	}

	/**
	 * Diese Methode prüft einen Request.
	 *
	 * @param request die zu prüfende Instanz
	 */
	private void verifyResult(CommonSolvePuzzleResponse request) {
		assertEquals("Elon Musk", request.getServerId());
		assertEquals(42L, request.getRaetselId());
		assertEquals(List.of(123, 456, 789), request.getRow1());
		assertEquals(List.of(444, 333, 222), request.getRow2());
		assertEquals(List.of(23, 211, 763), request.getRow3());
		assertEquals(2.3444, request.getTime(), 0.0001);
	}
}
