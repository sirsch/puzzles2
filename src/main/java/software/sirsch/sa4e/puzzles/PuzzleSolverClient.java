package software.sirsch.sa4e.puzzles;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.PuzzleSolverGrpc;
import software.sirsch.sa4e.puzzles.protobuf.PuzzleSolverGrpc.PuzzleSolverBlockingStub;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

/**
 * Diese Klasse stellt den gRPC-Client für {@link PuzzleSolverService} bereit.
 *
 * @author sirsch
 * @since 27.12.2022
 */
public class PuzzleSolverClient implements Closeable {

	/**
	 * Dieses Feld muss den Channel enthalten.
	 */
	@Nonnull
	private final ManagedChannel channel;

	/**
	 * Dieses Feld muss den Stub enthalten.
	 */
	@Nonnull
	private final PuzzleSolverBlockingStub stub;

	/**
	 * Dieses Feld muss den {@link Puzzle2ProtobufConverter} enthalten.
	 */
	@Nonnull
	private final Puzzle2ProtobufConverter puzzle2ProtobufConverter;

	/**
	 * Dieser Konstruktor initialisiert einen Channel mittels Host und Port.
	 *
	 * @param host der zu verwendende Name des Hosts
	 * @param port der zu verwendende Port
	 */
	public PuzzleSolverClient(@Nonnull final String host, final int port) {
		this(
				Grpc.newChannelBuilderForAddress(
						host,
						port,
						InsecureChannelCredentials.create()).build(),
				new Puzzle2ProtobufConverter());
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von Objekten zum Testen.
	 *
	 * @param channel der zu setzende Channel
	 * @param puzzle2ProtobufConverter der zu setzende Converter
	 */
	protected PuzzleSolverClient(
			@Nonnull final ManagedChannel channel,
			@Nonnull final Puzzle2ProtobufConverter puzzle2ProtobufConverter) {

		this.channel = channel;
		this.stub = PuzzleSolverGrpc.newBlockingStub(channel);
		this.puzzle2ProtobufConverter = puzzle2ProtobufConverter;
	}

	/**
	 * Diese Methode löst ein Rätsel, indem es das Rätsel zu einem Server zum Lösen sendet und die
	 * Antwort zurückgibt.
	 *
	 * @param puzzle das zu lösende Puzzle
	 * @return die ermittelte Antwort
	 * @throws RuntimeException falls das Rätsel keine Lösung hat
	 */
	public Map<Integer, Integer> solvePuzzle(@Nonnull final Puzzle puzzle) {
		return this.extractResult(this.stub.solvePuzzle(this.convertRequest(puzzle)));
	}

	/**
	 * Diese Methode erzeugt einen {@link SolvePuzzleRequest}.
	 *
	 * @param puzzle das zu untersuchende Puzzle
	 * @return die erzeugte Abfrage
	 */
	@Nonnull
	private SolvePuzzleRequest convertRequest(@Nonnull final Puzzle puzzle) {
		return this.puzzle2ProtobufConverter.createSolvePuzzleRequest(puzzle);
	}

	/**
	 * Diese Methode ermittelt das Ergebnis der Abfrage.
	 *
	 * @param response die auszuwertende Rückgabe
	 * @return die ermittelte Zuordnung von Symbol-ID zu Ziffernwert
	 */
	@Nonnull
	private Map<Integer, Integer> extractResult(@Nonnull final SolvePuzzleResponse response) {
		if (!response.getSolutionFound()) {
			throw new RuntimeException("Puzzle has no solution!");
		}

		return response.getSymbolIdToDigitMap();
	}

	@Override
	public void close() {
		try {
			this.channel.shutdownNow().awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
