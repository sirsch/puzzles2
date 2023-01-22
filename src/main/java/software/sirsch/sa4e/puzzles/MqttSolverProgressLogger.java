package software.sirsch.sa4e.puzzles;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

/**
 * Diese Klasse stellt einen {@link SolverProgressLogger} bereit, der die Nachrichten mittels
 * Eclipse Paho an einen MQTT-Server sendet.
 *
 * @author sirsch
 * @since 22.01.2023
 */
public class MqttSolverProgressLogger implements SolverProgressLogger {

	/**
	 * Dieses Feld muss den Client enthalten.
	 */
	@Nonnull
	private final MqttClient client;

	/**
	 * Dieser Konstruktor legt die URI des Servers fest.
	 *
	 * @param serverURI die zu setzende URI
	 */
	public MqttSolverProgressLogger(@Nonnull final String serverURI) {
		this(serverURI, MqttClient::new);
	}

	/**
	 * Dieser Konstruktor ermöglicht das Einschleusen von Objekten zum Testen.
	 *
	 * @param serverURI die zu setzende URI
	 * @param mqttClientFactory die zu setzende Fabrik für Clients
	 */
	protected MqttSolverProgressLogger(
			@Nonnull final String serverURI,
			@Nonnull final MqttClientFactory mqttClientFactory) {

		try {
			this.client = mqttClientFactory.create(serverURI, UUID.randomUUID().toString());
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void log(@Nonnull final String message) {
		try {
			if (!this.client.isConnected()) {
				this.client.connect();
			}

			this.client.publish("PuzzleSolverStatus", this.createMessage(message));
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Diese Methode erzeugt eine MQTT-Nachricht für eine Meldung.
	 *
	 * @param message die zu verwendende Meldung
	 * @return die erzeugte Nachricht
	 */
	private MqttMessage createMessage(@Nonnull final String message) {
		MqttMessage result = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));

		result.setQos(0);
		result.setRetained(true);
		return result;
	}

	/**
	 * Diese Schnittstelle beschreibt die Fabrikmethode für den Client.
	 */
	@FunctionalInterface
	protected interface MqttClientFactory {

		/**
		 * Diese Methode erzeugt einen neuen Client.
		 *
		 * @param serverURI die zu verwendende URI
		 * @param clientId die su verwendende Client-ID
		 * @return die erzeugte Instanz
		 * @throws MqttException zeigt einen Fehler bei der Erzeugung des Clients oder beim
		 * Herstellen der Verbindung an
		 */
		@Nonnull
		MqttClient create(String serverURI, String clientId) throws MqttException;
	}
}
