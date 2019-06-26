

package org.springframework.boot.web.client;

import org.springframework.web.client.RestTemplate;

/**
 * Callback interface that can be used to customize a {@link RestTemplate}.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @see RestTemplateBuilder
 */
@FunctionalInterface
public interface RestTemplateCustomizer {

	/**
	 * Callback to customize a {@link RestTemplate} instance.
	 * @param restTemplate the template to customize
	 */
	void customize(RestTemplate restTemplate);

}
