package software.sirsch.sa4e.puzzles;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import software.sirsch.sa4e.puzzles.protobuf.Puzzles;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest;
import software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link PuzzleSolverClient} bereit.
 *
 * @author sirsch
 * @since 28.12.2022
 */
public class PuzzleSolverClientTest {

	/**
	 * Dieses Feld soll den Mock für {@link PuzzleSolverService} enthalten.
	 */
	private PuzzleSolverService puzzleSolverService;

	/**
	 * Dieses Feld soll den Server zum Testen enthalten.
	 */
	private Server inprocServer;

	/**
	 * Dieses Feld soll den {@link ManagedChannel} zum Testen enthalten.
	 */
	private ManagedChannel channel;

	/**
	 * Dieses Feld soll den Mock für {@link Puzzle2ProtobufConverter} enthalten.
	 */
	private Puzzle2ProtobufConverter puzzle2ProtobufConverter;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleSolverClient objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@BeforeEach
	public void setUp() throws IOException {
		String uniqueName = InProcessServerBuilder.generateName();

		this.puzzleSolverService = mock(PuzzleSolverService.class);
		this.inprocServer = InProcessServerBuilder.forName(uniqueName)
				.directExecutor()
				.addService(this.puzzleSolverService)
				.build();
		this.channel = InProcessChannelBuilder.forName(uniqueName).build();
		this.puzzle2ProtobufConverter = mock(Puzzle2ProtobufConverter.class);
		this.inprocServer.start();

		this.objectUnderTest = new PuzzleSolverClient(this.channel, this.puzzle2ProtobufConverter);
	}

	/**
	 * Diese Methode räumt die Testumgebung nach jedem Testfall auf.
	 *
	 * @throws InterruptedException wird in diesem Testfall nicht erwartet
	 */
	@AfterEach
	public void tearDown() throws InterruptedException {
		assertTrue(this.inprocServer.shutdown().awaitTermination(1, TimeUnit.MINUTES));
		assertTrue(this.channel.shutdownNow().awaitTermination(1, TimeUnit.MINUTES));
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverClient#PuzzleSolverClient(String, int)}.
	 */
	@Test
	public void testHostPortConstructor() {
		this.objectUnderTest = new PuzzleSolverClient("localhost", 12345);

		assertThrows(
				StatusRuntimeException.class,
				() -> this.objectUnderTest.solvePuzzle(mock(Puzzle.class)));

		assertDoesNotThrow(() -> this.objectUnderTest.close());
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverClient#solvePuzzle(Puzzle)}.
	 */
	@Test
	public void testSolvePuzzle() {
		Puzzle puzzle = mock(Puzzle.class);
		SolvePuzzleRequest request = SolvePuzzleRequest.newBuilder()
				.addSymbols(Puzzles.Symbol.newBuilder()
						.setId(0)
						.setDescription("A")
						.build())
				.addSymbols(Puzzles.Symbol.newBuilder()
						.setId(1)
						.setDescription("B")
						.build())
				.addCells(Puzzles.Cell.newBuilder()
						.setRow(1)
						.setColumn(2)
						.addAllNumberAsSymbolIds(List.of(0, 1, 0)))
				.build();
		SolvePuzzleResponse response = SolvePuzzleResponse.newBuilder()
				.setSolutionFound(true)
				.putSymbolIdToDigit(1, 1)
				.putSymbolIdToDigit(2, 2)
				.build();
		Map<Integer, Integer> result;

		when(this.puzzle2ProtobufConverter.createSolvePuzzleRequest(puzzle)).thenReturn(request);
		doAnswer(invocation -> {
			assertEquals(request, invocation.getArgument(0));
			invocation.<StreamObserver<SolvePuzzleResponse>>getArgument(1).onNext(response);
			invocation.<StreamObserver<SolvePuzzleResponse>>getArgument(1).onCompleted();
			return null;
		}).when(this.puzzleSolverService).solvePuzzle(notNull(), notNull());

		result = this.objectUnderTest.solvePuzzle(puzzle);

		assertEquals(Map.of(1, 1, 2, 2), result);
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverClient#solvePuzzle(Puzzle)}.
	 */
	@Test
	public void testSolvePuzzleNoSolution() {
		Puzzle puzzle = mock(Puzzle.class);
		SolvePuzzleRequest request = SolvePuzzleRequest.newBuilder()
				.addSymbols(Puzzles.Symbol.newBuilder()
						.setId(0)
						.setDescription("A")
						.build())
				.addSymbols(Puzzles.Symbol.newBuilder()
						.setId(1)
						.setDescription("B")
						.build())
				.addCells(Puzzles.Cell.newBuilder()
						.setRow(1)
						.setColumn(2)
						.addAllNumberAsSymbolIds(List.of(0, 1, 0)))
				.build();
		SolvePuzzleResponse response = SolvePuzzleResponse.newBuilder()
				.setSolutionFound(false)
				.build();

		when(this.puzzle2ProtobufConverter.createSolvePuzzleRequest(puzzle)).thenReturn(request);
		doAnswer(invocation -> {
			assertEquals(request, invocation.getArgument(0));
			invocation.<StreamObserver<SolvePuzzleResponse>>getArgument(1).onNext(response);
			invocation.<StreamObserver<SolvePuzzleResponse>>getArgument(1).onCompleted();
			return null;
		}).when(this.puzzleSolverService).solvePuzzle(notNull(), notNull());

		assertThrows(RuntimeException.class, () -> this.objectUnderTest.solvePuzzle(puzzle));
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverClient#close()}.
	 *
	 * @throws InterruptedException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testClose() throws InterruptedException {
		ManagedChannel mockedChannel = mock(ManagedChannel.class);
		InOrder orderVerifier = inOrder(mockedChannel);

		when(mockedChannel.shutdownNow()).thenReturn(mockedChannel);
		this.objectUnderTest = new PuzzleSolverClient(mockedChannel, this.puzzle2ProtobufConverter);

		this.objectUnderTest.close();

		orderVerifier.verify(mockedChannel).shutdownNow();
		orderVerifier.verify(mockedChannel).awaitTermination(anyLong(), any());
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverClient#close()}, wenn dabei eine
	 * {@link InterruptedException} fliegt.
	 *
	 * @throws InterruptedException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testCloseInterruptedException() throws InterruptedException {
		ManagedChannel mockedChannel = mock(ManagedChannel.class);

		when(mockedChannel.shutdownNow()).thenReturn(mockedChannel);
		when(mockedChannel.awaitTermination(anyLong(), any())).thenThrow(new InterruptedException());
		this.objectUnderTest = new PuzzleSolverClient(mockedChannel, this.puzzle2ProtobufConverter);

		assertDoesNotThrow(() -> this.objectUnderTest.close());

		assertTrue(Thread.interrupted());
	}
}
