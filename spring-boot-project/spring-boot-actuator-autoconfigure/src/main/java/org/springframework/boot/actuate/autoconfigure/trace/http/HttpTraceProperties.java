

package org.springframework.boot.actuate.autoconfigure.trace.http;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.actuate.trace.http.Include;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for HTTP tracing.
 *
 * @author Wallace Wadge
 * @author Phillip Webb
 * @author Venil Noronha
 * @author Madhura Bhave
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "management.trace.http")
public class HttpTraceProperties {

	/**
	 * Items to be included in the trace. Defaults to request headers (excluding
	 * Authorization but including Cookie), response headers (including Set-Cookie), and
	 * time taken.
	 */
	private Set<Include> include = new HashSet<>(Include.defaultIncludes());

	public Set<Include> getInclude() {
		return this.include;
	}

	public void setInclude(Set<Include> include) {
		this.include = include;
	}

}
