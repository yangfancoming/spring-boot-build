

package org.springframework.boot.actuate.logging;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.logging.LogFile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Web {@link Endpoint} that provides access to an application's log file.
 *
 * @author Johannes Edmeier
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@WebEndpoint(id = "logfile")
public class LogFileWebEndpoint {

	private static final Log logger = LogFactory.getLog(LogFileWebEndpoint.class);

	private final Environment environment;

	private File externalFile;

	public LogFileWebEndpoint(Environment environment, File externalFile) {
		this.environment = environment;
		this.externalFile = externalFile;
	}

	public LogFileWebEndpoint(Environment environment) {
		this(environment, null);
	}

	@ReadOperation
	public Resource logFile() {
		Resource logFileResource = getLogFileResource();
		if (logFileResource == null || !logFileResource.isReadable()) {
			return null;
		}
		return logFileResource;
	}

	private Resource getLogFileResource() {
		if (this.externalFile != null) {
			return new FileSystemResource(this.externalFile);
		}
		LogFile logFile = LogFile.get(this.environment);
		if (logFile == null) {
			logger.debug("Missing 'logging.file' or 'logging.path' properties");
			return null;
		}
		return new FileSystemResource(logFile.toString());
	}

}
