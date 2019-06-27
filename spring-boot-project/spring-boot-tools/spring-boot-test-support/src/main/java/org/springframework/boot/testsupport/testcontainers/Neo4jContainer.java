

package org.springframework.boot.testsupport.testcontainers;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;
import org.rnorth.ducttape.TimeoutException;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

/**
 * A {@link GenericContainer} for Neo4J.
 *
 * @author Andy Wilkinson
 * @author Madhura Bhave
 */
public class Neo4jContainer extends Container {

	private static final int PORT = 7687;

	public Neo4jContainer() {
		super("neo4j:3.3.1", PORT, (container) -> container
				.waitingFor(new WaitStrategy(container)).withEnv("NEO4J_AUTH", "none"));
	}

	private static final class WaitStrategy extends HostPortWaitStrategy {

		private final GenericContainer<?> container;

		private WaitStrategy(GenericContainer<?> container) {
			this.container = container;
		}

		@Override
		public void waitUntilReady() {
			super.waitUntilReady();
			Configuration configuration = new Configuration.Builder()
					.uri("bolt://localhost:"
							+ this.container.getMappedPort(Neo4jContainer.PORT))
					.build();
			SessionFactory sessionFactory = new SessionFactory(configuration,
					"org.springframework.boot.test.autoconfigure.data.neo4j");
			try {
				Unreliables.retryUntilTrue((int) this.startupTimeout.getSeconds(),
						TimeUnit.SECONDS, checkConnection(sessionFactory));
			}
			catch (TimeoutException ex) {
				throw new IllegalStateException(ex);
			}
		}

		private Callable<Boolean> checkConnection(SessionFactory sessionFactory) {
			return () -> {
				try {
					sessionFactory.openSession().beginTransaction().close();
					return true;
				}
				catch (Exception ex) {
					return false;
				}
			};
		}

	}

}
