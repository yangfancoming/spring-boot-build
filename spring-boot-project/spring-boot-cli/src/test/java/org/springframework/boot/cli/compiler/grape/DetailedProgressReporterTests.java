

package org.springframework.boot.cli.compiler.grape;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferResource;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DetailedProgressReporter}.
 *
 * @author Andy Wilkinson
 */
public final class DetailedProgressReporterTests {

	private static final String REPOSITORY = "http://my.repository.com/";

	private static final String ARTIFACT = "org/alpha/bravo/charlie/1.2.3/charlie-1.2.3.jar";

	private final TransferResource resource = new TransferResource(REPOSITORY, ARTIFACT,
			null, null);

	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private final PrintStream out = new PrintStream(this.baos);

	private final DefaultRepositorySystemSession session = new DefaultRepositorySystemSession();

	@Before
	public void initialize() {
		new DetailedProgressReporter(this.session, this.out);
	}

	@Test
	public void downloading() throws TransferCancelledException {
		TransferEvent startedEvent = new TransferEvent.Builder(this.session,
				this.resource).build();
		this.session.getTransferListener().transferStarted(startedEvent);
		assertThat(new String(this.baos.toByteArray()))
				.isEqualTo(String.format("Downloading: %s%s%n", REPOSITORY, ARTIFACT));
	}

	@Test
	public void downloaded() throws InterruptedException {
		// Ensure some transfer time
		Thread.sleep(100);
		TransferEvent completedEvent = new TransferEvent.Builder(this.session,
				this.resource).addTransferredBytes(4096).build();
		this.session.getTransferListener().transferSucceeded(completedEvent);
		String message = new String(this.baos.toByteArray()).replace("\\", "/");
		assertThat(message).startsWith("Downloaded: " + REPOSITORY + ARTIFACT);
		assertThat(message).contains("4KB at");
		assertThat(message).contains("KB/sec");
		assertThat(message).endsWith("\n");
	}

}
