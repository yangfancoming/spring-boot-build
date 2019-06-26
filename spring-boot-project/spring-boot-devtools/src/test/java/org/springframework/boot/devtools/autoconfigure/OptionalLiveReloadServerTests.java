

package org.springframework.boot.devtools.autoconfigure;

import org.junit.Test;

import org.springframework.boot.devtools.livereload.LiveReloadServer;

import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link OptionalLiveReloadServer}.
 *
 * @author Phillip Webb
 */
public class OptionalLiveReloadServerTests {

	@Test
	public void nullServer() throws Exception {
		OptionalLiveReloadServer server = new OptionalLiveReloadServer(null);
		server.startServer();
		server.triggerReload();
	}

	@Test
	public void serverWontStart() throws Exception {
		LiveReloadServer delegate = mock(LiveReloadServer.class);
		OptionalLiveReloadServer server = new OptionalLiveReloadServer(delegate);
		willThrow(new RuntimeException("Error")).given(delegate).start();
		server.startServer();
		server.triggerReload();
		verify(delegate, never()).triggerReload();
	}

}
