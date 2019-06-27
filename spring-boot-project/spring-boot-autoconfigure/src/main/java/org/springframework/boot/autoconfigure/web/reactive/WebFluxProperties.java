

package org.springframework.boot.autoconfigure.web.reactive;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties properties} for Spring WebFlux.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "spring.webflux")
public class WebFluxProperties {

	/**
	 * Date format to use. For instance, `dd/MM/yyyy`.
	 */
	private String dateFormat;

	/**
	 * Path pattern used for static resources.
	 */
	private String staticPathPattern = "/**";

	public String getDateFormat() {
		return this.dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getStaticPathPattern() {
		return this.staticPathPattern;
	}

	public void setStaticPathPattern(String staticPathPattern) {
		this.staticPathPattern = staticPathPattern;
	}

}
