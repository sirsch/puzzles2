package software.sirsch.sa4e.puzzles;

import java.util.List;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import io.grpc.stub.StreamObserver;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link PuzzleSolverService} bereit.
 *
 * @author sirsch
 * @since 27.12.2022
 */
public class PuzzleSolverServiceTest {

	/**
	 * Dieses Feld soll den Mock für {@link Protobuf2PuzzleConverter} enthalten.
	 */
	private Protobuf2PuzzleConverter protobuf2PuzzleConverter;

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleSolver} enthalten.
	 */
	private PuzzleSolver puzzleSolver;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleSolverService objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.protobuf2PuzzleConverter = mock(Protobuf2PuzzleConverter.class);
		this.puzzleSolver = mock(PuzzleSolver.class);

		this.objectUnderTest = new PuzzleSolverService(
				this.protobuf2PuzzleConverter,
				this.puzzleSolver);
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverService#PuzzleSolverService()}.
	 */
	@Test
	public void testDefaultConstructor() {
		this.objectUnderTest = new PuzzleSolverService();

		assertThrows(
				IllegalArgumentException.class,
				() -> this.objectUnderTest.solvePuzzle(
						SolvePuzzleRequest.getDefaultInstance(),
						mock(StreamObserver.class)));
	}

	/**
	 * Diese Methode prüft
	 * {@link PuzzleSolverService#solvePuzzle(SolvePuzzleRequest, StreamObserver)}.
	 */
	@Test
	public void testSolvePuzzle() {
		Puzzle puzzle = mock(Puzzle.class);
		Symbol firstSymbol = mock(Symbol.class);
		Symbol secondSymbol = mock(Symbol.class);
		StreamObserver<SolvePuzzleResponse> streamObserver = mock(StreamObserver.class);
		InOrder orderVerifier = inOrder(streamObserver);
		SolvePuzzleResponse expectedResponse = SolvePuzzleResponse.newBuilder()
				.setSolutionFound(true)
				.putSymbolIdToDigit(0, 0)
				.putSymbolIdToDigit(1, 1)
				.build();

		when(firstSymbol.getId()).thenReturn(0);
		when(firstSymbol.getBoundValue()).thenReturn((byte) 0);
		when(secondSymbol.getId()).thenReturn(1);
		when(secondSymbol.getBoundValue()).thenReturn((byte) 1);
		when(this.protobuf2PuzzleConverter.createPuzzle(SolvePuzzleRequest.getDefaultInstance()))
				.thenReturn(puzzle);
		when(this.puzzleSolver.solvePuzzle(puzzle)).thenReturn(List.of(firstSymbol, secondSymbol));

		this.objectUnderTest.solvePuzzle(SolvePuzzleRequest.getDefaultInstance(), streamObserver);

		orderVerifier.verify(streamObserver).onNext(expectedResponse);
		orderVerifier.verify(streamObserver).onCompleted();
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode prüft
	 * {@link PuzzleSolverService#solvePuzzle(SolvePuzzleRequest, StreamObserver)}, wenn das Rätsel
	 * keine Lösung hat.
	 */
	@Test
	public void testSolvePuzzleNoSolution() {
		Puzzle puzzle = mock(Puzzle.class);
		StreamObserver<SolvePuzzleResponse> streamObserver = mock(StreamObserver.class);
		InOrder orderVerifier = inOrder(streamObserver);
		SolvePuzzleResponse expectedResponse = SolvePuzzleResponse.newBuilder()
				.setSolutionFound(false)
				.build();

		when(this.protobuf2PuzzleConverter.createPuzzle(SolvePuzzleRequest.getDefaultInstance()))
				.thenReturn(puzzle);
		when(this.puzzleSolver.solvePuzzle(puzzle)).thenReturn(List.of());

		this.objectUnderTest.solvePuzzle(SolvePuzzleRequest.getDefaultInstance(), streamObserver);

		orderVerifier.verify(streamObserver).onNext(expectedResponse);
		orderVerifier.verify(streamObserver).onCompleted();
		orderVerifier.verifyNoMoreInteractions();
	}
}
