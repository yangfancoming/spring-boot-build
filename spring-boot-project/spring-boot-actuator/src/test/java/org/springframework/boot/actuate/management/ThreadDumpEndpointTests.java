

package org.springframework.boot.actuate.management;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ThreadDumpEndpoint}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class ThreadDumpEndpointTests {

	@Test
	public void dumpThreads() {
		assertThat(new ThreadDumpEndpoint().threadDump().getThreads().size())
				.isGreaterThan(0);
	}

}
