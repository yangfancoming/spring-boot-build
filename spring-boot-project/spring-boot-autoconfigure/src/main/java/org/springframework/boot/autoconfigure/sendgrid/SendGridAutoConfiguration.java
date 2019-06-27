

package org.springframework.boot.autoconfigure.sendgrid;

import com.sendgrid.Client;
import com.sendgrid.SendGrid;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for SendGrid.
 *
 * @author Maciej Walkowiak
 * @author Patrick Bray
 * @author Andy Wilkinson
 * @since 1.3.0
 */
@Configuration
@ConditionalOnClass(SendGrid.class)
@ConditionalOnProperty(prefix = "spring.sendgrid", value = "api-key")
@EnableConfigurationProperties(SendGridProperties.class)
public class SendGridAutoConfiguration {

	private final SendGridProperties properties;

	public SendGridAutoConfiguration(SendGridProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public SendGrid sendGrid() {
		if (this.properties.isProxyConfigured()) {
			HttpHost proxy = new HttpHost(this.properties.getProxy().getHost(),
					this.properties.getProxy().getPort());
			return new SendGrid(this.properties.getApiKey(),
					new Client(HttpClientBuilder.create().setProxy(proxy).build()));
		}
		return new SendGrid(this.properties.getApiKey());
	}

}
