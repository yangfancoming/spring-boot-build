

package org.springframework.boot.web.servlet.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for {@link ErrorPageFilter}.
 *
 * @author Andy Wilkinson
 */
@Configuration
class ErrorPageFilterConfiguration {

	@Bean
	public ErrorPageFilter errorPageFilter() {
		return new ErrorPageFilter();
	}

}
