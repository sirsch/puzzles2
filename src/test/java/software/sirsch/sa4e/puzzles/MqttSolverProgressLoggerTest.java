package software.sirsch.sa4e.puzzles;

import java.util.Arrays;

import software.sirsch.sa4e.puzzles.MqttSolverProgressLogger.MqttClientFactory;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link MqttSolverProgressLogger} bereit.
 *
 * @author sirsch
 * @since 22.01.2023
 */
public class MqttSolverProgressLoggerTest {

	/**
	 * Dieses Feld soll den Mock für den Client enthalten.
	 */
	private MqttClient client;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private MqttSolverProgressLogger objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 *
	 * @throws MqttException wird in diesem Testfall nicht erwartet
	 */
	@BeforeEach
	public void setUp() throws MqttException {
		MqttClientFactory clientFactory = mock(MqttClientFactory.class);

		this.client = mock(MqttClient.class);
		when(clientFactory.create(eq("tcp://test.uri:1883"), anyString()))
				.thenReturn(this.client);

		this.objectUnderTest = new MqttSolverProgressLogger(
				"tcp://test.uri:1883",
				clientFactory);
	}

	/**
	 * Diese Methode prüft {@link MqttSolverProgressLogger#MqttSolverProgressLogger(String)}.
	 */
	@Test
	public void testConstructor() {
		assertDoesNotThrow(() -> new MqttSolverProgressLogger("tcp://dummy:1883"));
	}

	/**
	 * Diese Methode prüft die Behandlung von Ausnahmen bei
	 * {@link MqttSolverProgressLogger#MqttSolverProgressLogger(String, MqttClientFactory)}.
	 *
	 * @throws MqttException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testConstructorExceptionTranslation() throws MqttException {
		MqttClientFactory clientFactory = mock(MqttClientFactory.class);
		MqttException mqttException = new MqttException(42);
		RuntimeException caughtException;

		when(clientFactory.create(anyString(), anyString())).thenThrow(mqttException);

		caughtException = assertThrows(
				RuntimeException.class,
				() -> new MqttSolverProgressLogger("tcp://test.uri:1883", clientFactory));

		assertEquals(mqttException, caughtException.getCause());
	}

	/**
	 * Diese Methode prüft {@link MqttSolverProgressLogger#log(String)}, wenn noch keine Verbindung
	 * besteht.
	 *
	 * @throws MqttException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testLogNotConnected() throws MqttException {
		InOrder orderVerifier = inOrder(this.client);

		when(this.client.isConnected()).thenReturn(false);

		this.objectUnderTest.log("testMessage");

		orderVerifier.verify(this.client).connect();
		orderVerifier.verify(this.client).publish(
				eq("PuzzleSolverStatus"),
				argThat(message -> message.getQos() == 0
						&& Arrays.equals(message.getPayload(), "testMessage".getBytes(UTF_8))));
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode prüft {@link MqttSolverProgressLogger#log(String)}, wenn bereits eine
	 * Verbindung besteht.
	 *
	 * @throws MqttException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testLogAlreadyConnected() throws MqttException {
		InOrder orderVerifier = inOrder(this.client);

		when(this.client.isConnected()).thenReturn(true);

		this.objectUnderTest.log("testMessage");

		orderVerifier.verify(this.client).publish(
				eq("PuzzleSolverStatus"),
				argThat(message -> message.getQos() == 0
						&& Arrays.equals(message.getPayload(), "testMessage".getBytes(UTF_8))));
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode prüft die Behandlung von Ausnahmen bei
	 * {@link MqttSolverProgressLogger#log(String)}.
	 *
	 * @throws MqttException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testLogExceptionTranslation() throws MqttException {
		MqttException mqttException = new MqttException(42);
		RuntimeException caughtException;

		doThrow(mqttException).when(this.client).publish(anyString(), any());

		caughtException = assertThrows(
				RuntimeException.class,
				() -> this.objectUnderTest.log("testMessage"));

		assertEquals(mqttException, caughtException.getCause());
	}
}
