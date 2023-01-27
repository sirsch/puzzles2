package software.sirsch.sa4e.puzzles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
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
	 * Diese Konstante enthält den Schlüssel für den Header, der die ursprüngliche Anfrage enthält.
	 */
	private static final String ORIGINAL_REQUEST_HEADER
			= RunCamelCommand.class.getName() + ".ORIGINAL_REQUEST_HEADER";

	/**
	 * Diese Konstante enthält den Schlüssel für den Header, der den Startzeitpunkt enthält.
	 */
	private static final String START_TIMESTAMP_HEADER
			= RunCamelCommand.class.getName() + ".START_TIMESTAMP_HEADER";

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
						.process(RunCamelCommand.this::convertCommonFormat2Protobuf)
						.to(createGrpcSolvePuzzleUri())
						.filter(RunCamelCommand.this::isSolutionFound)
						.process(RunCamelCommand.this::mergeResult)
						.marshal().json(JsonLibrary.Jackson, true)
						.to(createMqttUri("Loesung"));
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
			public void configure() {
				from("timer:puzzleGenerator?period=60000")
						.process(RunCamelCommand.this::generatePuzzle)
						.marshal().json(JsonLibrary.Jackson, true)
						.to(createMqttUri("Zahlenraetsel"));
			}
		};
	}

	/**
	 * Diese Methode stellt einen Camel-Prozessor zur Konvertierung von Austauschdatenformat in das
	 * lokale Protobuf-Format bereit.
	 *
	 * @param exchange das zu verwendende Austauschobjekt
	 */
	private void convertCommonFormat2Protobuf(@Nonnull final Exchange exchange) {
		this.convertCommonFormat2Protobuf(
				exchange.getIn().getBody(CommonSolvePuzzleRequest.class),
				exchange.getMessage());
	}

	/**
	 * Diese Methode konvertiert ein Puzzle vom Austauschdatenformat in das lokale Protobuf-Format.
	 *
	 * @param inMessageBody der Inhalt der Eingangsnachricht
	 * @param outMessage die zu befüllende Ausgangsnachricht
	 */
	private void convertCommonFormat2Protobuf(
			@Nonnull final CommonSolvePuzzleRequest inMessageBody,
			@Nonnull final Message outMessage) {

		outMessage.setHeader(ORIGINAL_REQUEST_HEADER, inMessageBody);
		outMessage.setHeader(START_TIMESTAMP_HEADER, System.currentTimeMillis());
		outMessage.setBody(this.convertCommonFormat2Protobuf(inMessageBody));
	}

	/**
	 * Diese Methode konvertiert ein Puzzle vom Austauschdatenformat in das lokale Protobuf-Format.
	 *
	 * @param request das Puzzle im Austauschformat
	 * @return das Puzzle im Protobuf-Format
	 */
	private Puzzles.SolvePuzzleRequest convertCommonFormat2Protobuf(
			@Nonnull final CommonSolvePuzzleRequest request) {

		return new Puzzle2ProtobufConverter().createSolvePuzzleRequest(
				new Common2PuzzleConverter().createPuzzle(request));
	}

	/**
	 * Diese Methode stellt einen Camel-Predicate zur Prüfung, ob eine Lösung gefunden wurde,
	 * bereit.
	 *
	 * @param exchange das zu verwendende Austauschobjekt
	 * @return {@code true} falls eine Lösung gefunden wurde, sonst {@code false}
	 */
	private boolean isSolutionFound(@Nonnull final Exchange exchange) {
		return exchange.getIn().getBody(SolvePuzzleResponse.class).getSolutionFound();
	}

	/**
	 * Diese Methode stellt einen Camel-Prozessor zur Zusammenführung der Lösung mit dem Rätsel im
	 * Austauschdatenformat bereit.
	 *
	 * @param exchange das zu verwendende Austauschobjekt
	 */
	private void mergeResult(@Nonnull final Exchange exchange) {
		final double millisecondsPerSecond = 1000.0;
		CommonSolvePuzzleResponse response = new CommonSolvePuzzleResponse();

		response.setRaetselId(exchange.getIn().getHeader(
				ORIGINAL_REQUEST_HEADER,
				CommonSolvePuzzleRequest.class).getRaetselId());
		response.setServerId(exchange.getIn().getHeader(
				ORIGINAL_REQUEST_HEADER,
				CommonSolvePuzzleRequest.class).getServerId());
		response.setRow1(this.convertRow(
				exchange.getIn().getHeader(
						ORIGINAL_REQUEST_HEADER,
						CommonSolvePuzzleRequest.class).getRow1(),
				exchange.getIn().getBody(SolvePuzzleResponse.class).getSymbolIdToDigitMap()));
		response.setRow2(this.convertRow(
				exchange.getIn().getHeader(
						ORIGINAL_REQUEST_HEADER,
						CommonSolvePuzzleRequest.class).getRow2(),
				exchange.getIn().getBody(SolvePuzzleResponse.class).getSymbolIdToDigitMap()));
		response.setRow3(this.convertRow(
				exchange.getIn().getHeader(
						ORIGINAL_REQUEST_HEADER,
						CommonSolvePuzzleRequest.class).getRow3(),
				exchange.getIn().getBody(SolvePuzzleResponse.class).getSymbolIdToDigitMap()));
		response.setTime(
				(System.currentTimeMillis()
						- exchange.getIn().getHeader(START_TIMESTAMP_HEADER, Long.class))
						/ millisecondsPerSecond);
		exchange.getMessage().setBody(response);
	}

	/**
	 * Diese Methode konvertiert eine Zeile.
	 *
	 * @param row die zu konvertierende Zeile
	 * @param symbolIdToDigitMap die Zuordnung, für die Zeichenersetzung
	 * @return die konvertierte Zeile
	 */
	private List<Integer> convertRow(
			@Nonnull final List<String> row,
			@Nonnull final Map<Integer, Integer> symbolIdToDigitMap) {

		return row.stream()
				.map(cell -> this.convertCell(cell, symbolIdToDigitMap))
				.collect(Collectors.toList());
	}

	/**
	 * Diese Methode konvertiert eine Zelle.
	 *
	 * @param cell die zu konvertierende Zelle
	 * @param symbolIdToDigitMap die Zuordnung, für die Zeichenersetzung
	 * @return die konvertierte Zelle
	 */
	private Integer convertCell(
			@Nonnull final String cell,
			@Nonnull final Map<Integer, Integer> symbolIdToDigitMap) {

		StringBuilder stringBuilder = new StringBuilder();

		cell.chars()
				.map(symbolIdToDigitMap::get)
				.mapToObj(Integer::toString)
				.forEach(stringBuilder::append);
		return Integer.valueOf(stringBuilder.toString());
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
	 * Diese Methode erzeugte die Camel-URI für den gRPC-Endpoint.
	 *
	 * @return die erzeugte Camel-URI
	 */
	@Nonnull
	private String createGrpcSolvePuzzleUri() {
		return "grpc://" + this.grpcServer
				+ "/software.sirsch.sa4e.puzzles.protobuf.PuzzleSolver"
				+ "?method=solvePuzzle&synchronous=true";
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
