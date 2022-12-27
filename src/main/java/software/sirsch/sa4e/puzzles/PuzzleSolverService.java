package software.sirsch.sa4e.puzzles;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import software.sirsch.sa4e.puzzles.protobuf.PuzzleSolverGrpc;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse;

import io.grpc.stub.StreamObserver;

/**
 * Diese Klasse stellt die Implementierung für {@link PuzzleSolverGrpc} bereit.
 *
 * @author sirsch
 * @since 27.12.2022
 */
public class PuzzleSolverService extends PuzzleSolverGrpc.PuzzleSolverImplBase {

	/**
	 * Dieses Feld muss den {@link Protobuf2PuzzleConverter} enthalten.
	 */
	@Nonnull
	private final Protobuf2PuzzleConverter protobuf2PuzzleConverter;

	/**
	 * Dieses Feld muss den {@link PuzzleSolver} enthalten.
	 */
	@Nonnull
	private final PuzzleSolver puzzleSolver;

	/**
	 * Dieser Konstruktor nimmt die interne Initialisierung vor.
	 */
	public PuzzleSolverService() {
		this(new Protobuf2PuzzleConverter(), new PuzzleSolver());
	}

	/**
	 * Dieser Konstruktor erlaubt das Einschleusen von Objekten zum Testen.
	 *
	 * @param protobuf2PuzzleConverter der zu setzende {@link Protobuf2PuzzleConverter}
	 * @param puzzleSolver der zu setzende {@link PuzzleSolver}
	 */
	protected PuzzleSolverService(
			@Nonnull final Protobuf2PuzzleConverter protobuf2PuzzleConverter,
			@Nonnull final PuzzleSolver puzzleSolver) {

		this.protobuf2PuzzleConverter = protobuf2PuzzleConverter;
		this.puzzleSolver = puzzleSolver;
	}

	@Override
	public void solvePuzzle(
			@Nonnull final SolvePuzzleRequest request,
			@Nonnull final StreamObserver<SolvePuzzleResponse> responseObserver) {

		responseObserver.onNext(this.solvePuzzle(request));
		responseObserver.onCompleted();
	}

	/**
	 * Diese Methode löst ein Rätsel.
	 *
	 * @param request die auszuwertende Anfrage
	 * @return die erzeugte Antwort
	 */
	private SolvePuzzleResponse solvePuzzle(@Nonnull final SolvePuzzleRequest request) {
		return this.generateResponse(this.puzzleSolver.solvePuzzle(this.convertRequest(request)));
	}

	/**
	 * Diese Methode überträgt einen {@link SolvePuzzleRequest} in ein {@link Puzzle}.
	 *
	 * @param request die auszuwertende Anfrage
	 * @return das ermittelte Rätsel
	 */
	private Puzzle convertRequest(@Nonnull final SolvePuzzleRequest request) {
		return this.protobuf2PuzzleConverter.createPuzzle(request);
	}

	/**
	 * Diese Methode erzeugt die {@link SolvePuzzleResponse}.
	 *
	 * <p>
	 *     Eine leere Liste bedeutet, dass keine Lösung gefunden wurde.
	 * </p>
	 *
	 * @param result die Belegung der Symbole
	 * @return die erzeugte Antwort
	 */
	private SolvePuzzleResponse generateResponse(@Nonnull final List<Symbol> result) {
		return SolvePuzzleResponse.newBuilder()
				.setSolutionFound(!result.isEmpty())
				.putAllSymbolIdToDigit(this.collectSymbolIdToDigitMap(result))
				.build();
	}

	/**
	 * Diese Methode gibt die Zuordnung von Symbol-Id zu Symbol-Wert zurück.
	 *
	 * @param result das zu untersuchende Ergebnis
	 * @return die erzeugte Zuordnung
	 */
	private Map<Integer, Integer> collectSymbolIdToDigitMap(@Nonnull final List<Symbol> result) {
		return result.stream()
				.collect(Collectors.toMap(Symbol::getId, symbol -> (int) symbol.getBoundValue()));
	}
}
