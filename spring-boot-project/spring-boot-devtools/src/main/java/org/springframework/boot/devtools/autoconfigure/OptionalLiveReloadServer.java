

package org.springframework.boot.devtools.autoconfigure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.devtools.livereload.LiveReloadServer;

/**
 * Manages an optional {@link LiveReloadServer}. The {@link LiveReloadServer} may
 * gracefully fail to start (e.g. because of a port conflict) or may be omitted entirely.
 *
 * @author Phillip Webb
 * @since 1.3.0
 */
public class OptionalLiveReloadServer implements InitializingBean {

	private static final Log logger = LogFactory.getLog(OptionalLiveReloadServer.class);

	private LiveReloadServer server;

	/**
	 * Create a new {@link OptionalLiveReloadServer} instance.
	 * @param server the server to manage or {@code null}
	 */
	public OptionalLiveReloadServer(LiveReloadServer server) {
		this.server = server;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		startServer();
	}

	void startServer() throws Exception {
		if (this.server != null) {
			try {
				if (!this.server.isStarted()) {
					this.server.start();
				}
				logger.info(
						"LiveReload server is running on port " + this.server.getPort());
			}
			catch (Exception ex) {
				logger.warn("Unable to start LiveReload server");
				logger.debug("Live reload start error", ex);
				this.server = null;
			}
		}
	}

	/**
	 * Trigger LiveReload if the server is up and running.
	 */
	public void triggerReload() {
		if (this.server != null) {
			this.server.triggerReload();
		}
	}

}
