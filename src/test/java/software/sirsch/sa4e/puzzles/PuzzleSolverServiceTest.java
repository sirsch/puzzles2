package software.sirsch.sa4e.puzzles;

import java.util.List;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse;

import org.apache.commons.collections4.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import io.grpc.stub.StreamObserver;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.notNull;
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
	 * Dieses Feld soll den Mock für die Fabrik für {@link Protobuf2PuzzleConverter} enthalten.
	 */
	private Factory<Protobuf2PuzzleConverter> protobuf2PuzzleConverterFactory;

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
		this.protobuf2PuzzleConverterFactory = mock(Factory.class);
		this.puzzleSolver = mock(PuzzleSolver.class);

		this.objectUnderTest = new PuzzleSolverService(
				this.protobuf2PuzzleConverterFactory,
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
		Protobuf2PuzzleConverter converter = mock(Protobuf2PuzzleConverter.class);
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
		when(this.protobuf2PuzzleConverterFactory.create()).thenReturn(converter, null);
		when(converter.createPuzzle(SolvePuzzleRequest.getDefaultInstance()))
				.thenReturn(puzzle, null);
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
		Protobuf2PuzzleConverter converter = mock(Protobuf2PuzzleConverter.class);
		Puzzle puzzle = mock(Puzzle.class);
		StreamObserver<SolvePuzzleResponse> streamObserver = mock(StreamObserver.class);
		InOrder orderVerifier = inOrder(streamObserver);
		SolvePuzzleResponse expectedResponse = SolvePuzzleResponse.newBuilder()
				.setSolutionFound(false)
				.build();

		when(this.protobuf2PuzzleConverterFactory.create()).thenReturn(converter, null);
		when(converter.createPuzzle(SolvePuzzleRequest.getDefaultInstance()))
				.thenReturn(puzzle, null);
		when(this.puzzleSolver.solvePuzzle(puzzle)).thenReturn(List.of());

		this.objectUnderTest.solvePuzzle(SolvePuzzleRequest.getDefaultInstance(), streamObserver);

		orderVerifier.verify(streamObserver).onNext(expectedResponse);
		orderVerifier.verify(streamObserver).onCompleted();
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode prüft den mehrfachen Aufruf von
	 * {@link PuzzleSolverService#solvePuzzle(SolvePuzzleRequest, StreamObserver)}.
	 *
	 * <p>
	 *     Dabei wird insbesondere geprüft, dass für jede Anfrage ein eigener
	 *     {@link Protobuf2PuzzleConverter} erzeugt wird.
	 * </p>
	 */
	@Test
	public void testSolveTwoPuzzles() {
		Protobuf2PuzzleConverter firstConverter = mock(Protobuf2PuzzleConverter.class);
		Protobuf2PuzzleConverter secondConverter = mock(Protobuf2PuzzleConverter.class);
		Puzzle puzzle = mock(Puzzle.class);
		Symbol firstSymbol = mock(Symbol.class);
		Symbol secondSymbol = mock(Symbol.class);
		StreamObserver<SolvePuzzleResponse> streamObserver = mock(StreamObserver.class);
		InOrder orderVerifier = inOrder(firstConverter, secondConverter, streamObserver);
		SolvePuzzleResponse expectedResponse = SolvePuzzleResponse.newBuilder()
				.setSolutionFound(true)
				.putSymbolIdToDigit(0, 0)
				.putSymbolIdToDigit(1, 1)
				.build();

		when(firstSymbol.getId()).thenReturn(0);
		when(firstSymbol.getBoundValue()).thenReturn((byte) 0);
		when(secondSymbol.getId()).thenReturn(1);
		when(secondSymbol.getBoundValue()).thenReturn((byte) 1);
		when(this.protobuf2PuzzleConverterFactory.create()).thenReturn(
				firstConverter,
				secondConverter,
				null);
		when(firstConverter.createPuzzle(SolvePuzzleRequest.getDefaultInstance()))
				.thenReturn(puzzle, null);
		when(secondConverter.createPuzzle(SolvePuzzleRequest.getDefaultInstance()))
				.thenReturn(puzzle, null);
		when(this.puzzleSolver.solvePuzzle(puzzle)).thenReturn(List.of(firstSymbol, secondSymbol));

		this.objectUnderTest.solvePuzzle(SolvePuzzleRequest.getDefaultInstance(), streamObserver);
		this.objectUnderTest.solvePuzzle(SolvePuzzleRequest.getDefaultInstance(), streamObserver);

		orderVerifier.verify(firstConverter).createPuzzle(notNull());
		orderVerifier.verify(streamObserver).onNext(expectedResponse);
		orderVerifier.verify(streamObserver).onCompleted();
		orderVerifier.verify(secondConverter).createPuzzle(notNull());
		orderVerifier.verify(streamObserver).onNext(expectedResponse);
		orderVerifier.verify(streamObserver).onCompleted();
		orderVerifier.verifyNoMoreInteractions();
	}
}
