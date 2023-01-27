package software.sirsch.sa4e.puzzles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.UUID;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * Diese Klasse stellt das Kommando zum Betreiben von Camel bereit.
 *
 * @author sirsch
 * @since 25.01.2023
 */
public class RunCamelCommand implements Command {

	/**
	 * Diese Konstante enthält den Namen des Kommandos.
	 */
	public static final String COMMAND_NAME = "run-camel";

	/**
	 * Dieses Feld soll die URL des MQTT-Brokers enthalten.
	 */
	@CheckForNull
	private String mqttBrokerUrl;

	/**
	 * Dieses Feld soll Host und Port des gRPC-Servers enthalten.
	 */
	@CheckForNull
	private String grpcServer;

	/**
	 * Dieses Feld kann die Anzahl der Stellen enthalten, falls Rätsel generiert werden sollen.
	 */
	@CheckForNull
	private Integer numberOfDigits;

	/**
	 * Dieses Feld enthält die Server-ID.
	 */
	@Nonnull
	private final String serverId = "sirsch " + UUID.randomUUID();

	/**
	 * Diese Methode führt das Kommando aus.
	 *
	 * <p>
	 *     Dabei wird als Argument der Dateiname für die Ausgabedatei erwartet.
	 * </p>
	 *
	 * @param args die Argumente
	 */
	@Override
	public void execute(@Nonnull final String... args) {
		this.mqttBrokerUrl = this.extractMqttBrokerUrl(args);
		this.grpcServer = this.extractGrpcServer(args);
		this.numberOfDigits = this.extractGeneratePuzzleNumberOfDigits(args);

		this.runCamel();
	}

	/**
	 * Diese Methode ermittelt die URL des MQTT-Brokers.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return die ermittelte URL
	 */
	@Nonnull
	private String extractMqttBrokerUrl(@Nonnull final String[] args) {
		if (args.length <= 1) {
			throw new IllegalArgumentException("usage: "
					+ "run-camel <mqttBrokerUrl> <grpcServer> <?generatePuzzleNumberOfDigits>");
		}

		return args[1];
	}

	/**
	 * Diese Methode ermittelt den gRPC-Server.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return der ermittelte Server
	 */
	@Nonnull
	private String extractGrpcServer(@Nonnull final String[] args) {
		if (args.length <= 2) {
			throw new IllegalArgumentException("usage: "
					+ "run-camel <mqttBrokerUrl> <grpcServer> <?generatePuzzleNumberOfDigits>");
		}

		return args[2];
	}

	/**
	 * Diese Methode ermittelt den gRPC-Server.
	 *
	 * @param args die zu übergebenen Argumente
	 * @return der ermittelte Server
	 */
	@Nonnull
	private Integer extractGeneratePuzzleNumberOfDigits(@Nonnull final String[] args) {
		final int index = 3;

		if (args.length <= index) {
			return null;
		}

		return Integer.valueOf(args[index]);
	}

	/**
	 * Diese Methode führt das Camel aus.
	 */
	private void runCamel() {
		try (CamelContext camel = new DefaultCamelContext()) {
			camel.addRoutes(this.createDefaultRouteBuilder());

			if (this.numberOfDigits != null) {
				camel.addRoutes(this.createGeneratorRouteBuilder());
			}

			camel.start();
			this.awaitShutdown();
			camel.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Diese Methode erzeugt die Camel-Route, für das Laden von Rätseln von MQTT 'Zahlenraetsel',
	 * Lösen per gRPC-Service und Übertragen der Löusung an MQTT 'Loesung'.
	 *
	 * @return der erzeugt {@link RouteBuilder}
	 */
	@Nonnull
	private RouteBuilder createDefaultRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(createMqttUri("Zahlenraetsel"))
						.unmarshal().json(JsonLibrary.Jackson, CommonSolvePuzzleRequest.class)
						.process(exchange -> exchange.getMessage().setBody(
								new Puzzle2ProtobufConverter().createSolvePuzzleRequest(
										new Common2PuzzleConverter().createPuzzle(
												exchange.getIn().getBody(
														CommonSolvePuzzleRequest.class)))))
						.to("grpc://" + grpcServer
								+ "/software.sirsch.sa4e.puzzles.protobuf.PuzzleSolver"
								+ "?method=solvePuzzle&synchronous=true")
						.process()
						.body(System.out::println)
						.log("log message");
			}
		};
	}

	/**
	 * Diese Methode erzeugt die Camel-Route, die jede Minute ein Puzzle erzeugt und an MQTT sendet.
	 *
	 * @return der erzeugt {@link RouteBuilder}
	 */
	@Nonnull
	private RouteBuilder createGeneratorRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("timer:puzzleGenerator?period=60000")
						.process(RunCamelCommand.this::generatePuzzle)
						.marshal().json(JsonLibrary.Jackson, true)
						.to(createMqttUri("Zahlenraetsel"));
			}
		};
	}

	/**
	 * Diese Methode stellt einen Camel-Prozessor zur Erzeugung eines Puzzles bereit.
	 *
	 * @param exchange das zu verwendende Austauschobjekt
	 */
	private void generatePuzzle(@Nonnull final Exchange exchange) {
		exchange.getMessage().setBody(this.generatePuzzle());
	}

	/**
	 * Diese Methode erzeugt ein neues Puzzle und verpackt es in einen
	 * {@link CommonSolvePuzzleRequest}.
	 *
	 * @return die erzeugte Instanz
	 */
	private CommonSolvePuzzleRequest generatePuzzle() {
		CommonSolvePuzzleRequest request =
				new Puzzle2CommonConverter().createCommonSolvePuzzleRequest(
						new PuzzleGenerator().generate(this.numberOfDigits));

		request.setServerId(this.serverId);
		request.setRaetselId((long) new Random().nextInt(Byte.MAX_VALUE));
		return request;
	}

	/**
	 * Diese Methode erzeugte die Camel-URI für einen MQTT-Endpoint.
	 *
	 * @param topic das zu verwendende Topic
	 * @return die erzeugte Camel-URI
	 */
	@Nonnull
	private String createMqttUri(@Nonnull final String topic) {
		return "paho-mqtt5:" + topic + "?brokerUrl=" + this.mqttBrokerUrl;
	}

	/**
	 * Diese Methode wartet auf die Benutzereingabe zum Beenden des Programms.
	 *
	 * @throws IOException zeigt einen Datenübertragungsfehler an
	 */
	private void awaitShutdown() throws IOException {
		System.out.println("Press return to quit!");
		new BufferedReader(new InputStreamReader(System.in)).readLine();
	}
}
