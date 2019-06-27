

package org.springframework.boot.autoconfigure.elasticsearch.jest;

import java.time.Duration;
import java.util.List;

import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.HttpHost;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties.Proxy;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Jest.
 *
 * @author Stephane Nicoll
 * @since 1.4.0
 */
@Configuration
@ConditionalOnClass(JestClient.class)
@EnableConfigurationProperties(JestProperties.class)
@AutoConfigureAfter(GsonAutoConfiguration.class)
public class JestAutoConfiguration {

	private final JestProperties properties;

	private final ObjectProvider<Gson> gsonProvider;

	private final List<HttpClientConfigBuilderCustomizer> builderCustomizers;

	public JestAutoConfiguration(JestProperties properties, ObjectProvider<Gson> gson,
			ObjectProvider<List<HttpClientConfigBuilderCustomizer>> builderCustomizers) {
		this.properties = properties;
		this.gsonProvider = gson;
		this.builderCustomizers = builderCustomizers.getIfAvailable();
	}

	@Bean(destroyMethod = "shutdownClient")
	@ConditionalOnMissingBean
	public JestClient jestClient() {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(createHttpClientConfig());
		return factory.getObject();
	}

	protected HttpClientConfig createHttpClientConfig() {
		HttpClientConfig.Builder builder = new HttpClientConfig.Builder(
				this.properties.getUris());
		PropertyMapper map = PropertyMapper.get();
		map.from(this.properties::getUsername).whenHasText().to((username) -> builder
				.defaultCredentials(username, this.properties.getPassword()));
		Proxy proxy = this.properties.getProxy();
		map.from(proxy::getHost).whenHasText().to((host) -> {
			Assert.notNull(proxy.getPort(), "Proxy port must not be null");
			builder.proxy(new HttpHost(host, proxy.getPort()));
		});
		map.from(this.gsonProvider::getIfUnique).whenNonNull().to(builder::gson);
		map.from(this.properties::isMultiThreaded).to(builder::multiThreaded);
		map.from(this.properties::getConnectionTimeout).whenNonNull()
				.asInt(Duration::toMillis).to(builder::connTimeout);
		map.from(this.properties::getReadTimeout).whenNonNull().asInt(Duration::toMillis)
				.to(builder::readTimeout);
		customize(builder);
		return builder.build();
	}

	private void customize(HttpClientConfig.Builder builder) {
		if (this.builderCustomizers != null) {
			for (HttpClientConfigBuilderCustomizer customizer : this.builderCustomizers) {
				customizer.customize(builder);
			}
		}
	}

}
