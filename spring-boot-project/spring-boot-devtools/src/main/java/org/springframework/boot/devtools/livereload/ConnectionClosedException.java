

package org.springframework.boot.devtools.livereload;

import java.io.IOException;

/**
 * Exception throw when the client closes the connection.
 *
 * @author Phillip Webb
 */
class ConnectionClosedException extends IOException {

	ConnectionClosedException() {
		super("Connection closed");
	}

}
