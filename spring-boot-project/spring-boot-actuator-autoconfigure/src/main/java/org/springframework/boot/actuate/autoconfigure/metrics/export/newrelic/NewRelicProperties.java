

package org.springframework.boot.actuate.autoconfigure.metrics.export.newrelic;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for configuring New Relic metrics export.
 *
 * @author Jon Schneider
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "management.metrics.export.newrelic")
public class NewRelicProperties extends StepRegistryProperties {

	/**
	 * New Relic API key.
	 */
	private String apiKey;

	/**
	 * New Relic account ID.
	 */
	private String accountId;

	/**
	 * URI to ship metrics to.
	 */
	private String uri = "https://insights-collector.newrelic.com";

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
