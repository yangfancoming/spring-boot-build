

package org.springframework.boot.testsupport.testcontainers;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import org.rnorth.ducttape.TimeoutException;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

/**
 * A {@link GenericContainer} for Cassandra.
 *
 * @author Andy Wilkinson
 * @author Madhura Bhave
 */
public class CassandraContainer extends Container {

	private static final int PORT = 9042;

	public CassandraContainer() {
		super("cassandra:3.11.1", PORT,
				(container) -> container.waitingFor(new WaitStrategy(container))
						.withStartupAttempts(5)
						.withStartupTimeout(Duration.ofSeconds(120)));
	}

	private static final class WaitStrategy extends HostPortWaitStrategy {

		private final GenericContainer<?> container;

		private WaitStrategy(GenericContainer<?> container) {
			this.container = container;
		}

		@Override
		public void waitUntilReady() {
			super.waitUntilReady();

			try {
				Unreliables.retryUntilTrue((int) this.startupTimeout.getSeconds(),
						TimeUnit.SECONDS, checkConnection());
			}
			catch (TimeoutException ex) {
				throw new IllegalStateException(ex);
			}
		}

		private Callable<Boolean> checkConnection() {
			return () -> {
				try (Cluster cluster = Cluster.builder()
						.withPort(this.container.getMappedPort(CassandraContainer.PORT))
						.addContactPoint("localhost").build()) {
					cluster.connect();
					return true;
				}
				catch (IllegalArgumentException | NoHostAvailableException ex) {
					return false;
				}
			};
		}

	}

}
