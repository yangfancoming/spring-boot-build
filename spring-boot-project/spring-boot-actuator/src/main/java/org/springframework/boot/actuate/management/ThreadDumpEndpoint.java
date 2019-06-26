

package org.springframework.boot.actuate.management;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

/**
 * {@link Endpoint} to expose thread info.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@Endpoint(id = "threaddump")
public class ThreadDumpEndpoint {

	@ReadOperation
	public ThreadDumpDescriptor threadDump() {
		return new ThreadDumpDescriptor(Arrays
				.asList(ManagementFactory.getThreadMXBean().dumpAllThreads(true, true)));
	}

	/**
	 * A description of a thread dump. Primarily intended for serialization to JSON.
	 */
	public static final class ThreadDumpDescriptor {

		private final List<ThreadInfo> threads;

		private ThreadDumpDescriptor(List<ThreadInfo> threads) {
			this.threads = threads;
		}

		public List<ThreadInfo> getThreads() {
			return this.threads;
		}

	}

}
