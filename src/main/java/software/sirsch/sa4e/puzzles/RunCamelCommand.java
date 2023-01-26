package software.sirsch.sa4e.puzzles;

import javax.annotation.Nonnull;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

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
		this.runCamel();
	}

	/**
	 * Diese Methode führt das Camel aus.
	 */
	private void runCamel() {
		final long sleepTimeMillis = 10000;

		try (CamelContext camel = new DefaultCamelContext()) {
			camel.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
//					from("timer:simple?period=503")
//					from("paho-mqtt5:test?brokerUrl=tcp://localhost:1883")
//							.process()
//							.message(System.out::println)
//							.process()
//							.body(System.out::println)
//							.log("log message");
					from("timer:simple?period=503")
							.process(exchange -> exchange.getMessage().setBody(
									new Puzzle2ProtobufConverter().createSolvePuzzleRequest(
											new PuzzleGenerator().generate(2))))
							.to("grpc://localhost:12345/"
									+ "software.sirsch.sa4e.puzzles.protobuf.PuzzleSolver"
									+ "?method=solvePuzzle&synchronous=true")
							.process()
							.body(System.out::println)
							.log("log message");
				}
			});

			camel.start();
			Thread.sleep(sleepTimeMillis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
