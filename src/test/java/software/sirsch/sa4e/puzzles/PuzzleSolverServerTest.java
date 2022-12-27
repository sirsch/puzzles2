package software.sirsch.sa4e.puzzles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import io.grpc.Server;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse stellt Tests für {@link PuzzleSolverServer} bereit.
 *
 * @author sirsch
 * @since 27.12.2022
 */
public class PuzzleSolverServerTest {

	/**
	 * Dieses Feld soll den Mock für {@link Server} enthalten.
	 */
	private Server server;

	/**
	 * Dieses Feld soll den Mock für {@link Runtime#addShutdownHook(Thread)} enthalten.
	 */
	private Consumer<Thread> shutdownHookAdder;

	/**
	 * Dieses Feld soll das zu testende Objekt enthalten.
	 */
	private PuzzleSolverServer objectUnderTest;

	/**
	 * Diese Methode bereitet die Testumgebung für jeden Testfall vor.
	 */
	@BeforeEach
	public void setUp() {
		this.server = mock(Server.class);
		this.shutdownHookAdder = mock(Consumer.class);
		when(this.server.shutdown()).thenReturn(this.server);

		this.objectUnderTest = new PuzzleSolverServer(this.server, this.shutdownHookAdder);
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#PuzzleSolverServer(int)}.
	 */
	@Test
	public void testConstructor() {
		this.objectUnderTest = new PuzzleSolverServer(0);

		this.objectUnderTest.start();
		this.objectUnderTest.stop();
		this.objectUnderTest.stop();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#run()}.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 * @throws InterruptedException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testRun() throws IOException, InterruptedException {
		InOrder orderVerifier = inOrder(this.server, this.shutdownHookAdder);

		this.objectUnderTest.run();

		orderVerifier.verify(this.shutdownHookAdder).accept(notNull());
		orderVerifier.verify(this.server).start();
		orderVerifier.verify(this.server).awaitTermination();
		orderVerifier.verifyNoMoreInteractions();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#registerShutdownHook()}.
	 */
	@Test
	public void testRegisterShutdownHook() {
		ArgumentCaptor<Thread> threadArgumentCaptor = ArgumentCaptor.forClass(Thread.class);

		this.objectUnderTest.registerShutdownHook();

		verify(this.shutdownHookAdder).accept(threadArgumentCaptor.capture());
		assertNotNull(threadArgumentCaptor.getValue());
		verify(this.server, never()).shutdown();
		threadArgumentCaptor.getValue().run();
		verify(this.server).shutdown();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#start()}.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testStart() throws IOException {
		this.objectUnderTest.start();

		verify(this.server).start();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#start()}, wenn eine {@link IOException} fliegt.
	 *
	 * @throws IOException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testStartExceptionTranslation() throws IOException {
		IOException testException = new IOException("testException");
		UncheckedIOException caughtException;

		doThrow(testException).when(this.server).start();

		caughtException = assertThrows(
				UncheckedIOException.class,
				() -> this.objectUnderTest.start());

		assertEquals("java.io.IOException: testException", caughtException.getMessage());
		assertEquals(testException, caughtException.getCause());
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#stop()}.
	 */
	@Test
	public void testStop() {
		this.objectUnderTest.stop();

		verify(this.server).shutdown();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#stop()} wenn eine {@link InterruptedException}
	 * fliegt.
	 *
	 * @throws InterruptedException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testStopInterruptedException() throws InterruptedException {
		doThrow(new InterruptedException()).when(this.server).awaitTermination(anyLong(), any());

		assertDoesNotThrow(() -> this.objectUnderTest.stop());

		assertTrue(Thread.interrupted());
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#awaitTermination()}.
	 *
	 * @throws InterruptedException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testAwaitTermination() throws InterruptedException {
		this.objectUnderTest.awaitTermination();

		verify(this.server).awaitTermination();
	}

	/**
	 * Diese Methode prüft {@link PuzzleSolverServer#awaitTermination()} wenn eine
	 * {@link InterruptedException} fliegt.
	 *
	 * @throws InterruptedException wird in diesem Testfall nicht erwartet
	 */
	@Test
	public void testAwaitTerminationInterruptedException() throws InterruptedException {
		doThrow(new InterruptedException()).when(this.server).awaitTermination();

		assertDoesNotThrow(() -> this.objectUnderTest.awaitTermination());

		assertTrue(Thread.interrupted());
	}
}
