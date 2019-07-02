

package org.springframework.boot.test.autoconfigure.web.servlet;

import com.gargoylesoftware.htmlunit.WebClient;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.web.htmlunit.LocalHostWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

/**
 * Auto-configuration for HtmlUnit {@link WebClient} MockMVC integration.
 *
 * @author Phillip Webb
 * @since 1.4.0
 */
@Configuration
@ConditionalOnClass(WebClient.class)
@AutoConfigureAfter(MockMvcAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.test.mockmvc.webclient", name = "enabled", matchIfMissing = true)
public class MockMvcWebClientAutoConfiguration {

	private final Environment environment;

	MockMvcWebClientAutoConfiguration(Environment environment) {
		this.environment = environment;
	}

	@Bean
	@ConditionalOnMissingBean({ WebClient.class, MockMvcWebClientBuilder.class })
	@ConditionalOnBean(MockMvc.class)
	public MockMvcWebClientBuilder mockMvcWebClientBuilder(MockMvc mockMvc) {
		return MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
				.withDelegate(new LocalHostWebClient(this.environment));
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(MockMvcWebClientBuilder.class)
	public WebClient htmlUnitWebClient(MockMvcWebClientBuilder builder) {
		return builder.build();
	}

}
