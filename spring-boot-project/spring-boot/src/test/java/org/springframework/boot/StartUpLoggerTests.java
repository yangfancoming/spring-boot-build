

package org.springframework.boot;

import org.apache.commons.logging.Log;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link StartupInfoLogger}.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
 */
public class StartUpLoggerTests {

	private final Log log = mock(Log.class);

	@Test
	public void sourceClassIncluded() {
		given(this.log.isInfoEnabled()).willReturn(true);
		new StartupInfoLogger(getClass()).logStarting(this.log);
		verify(this.log).info(startsWith("Starting " + getClass().getSimpleName()));
	}

}
