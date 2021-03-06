

package org.springframework.boot.test.autoconfigure.web.servlet;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.web.htmlunit.webdriver.LocalHostWebConnectionHtmlUnitDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;

/**
 * Auto-configuration for Selenium {@link WebDriver} MockMVC integration.
 *
 * @author Phillip Webb
 * @since 1.4.0
 */
@Configuration
@ConditionalOnClass(HtmlUnitDriver.class)
@AutoConfigureAfter(MockMvcAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.test.mockmvc.webdriver", name = "enabled", matchIfMissing = true)
public class MockMvcWebDriverAutoConfiguration {

	private final Environment environment;

	MockMvcWebDriverAutoConfiguration(Environment environment) {
		this.environment = environment;
	}

	@Bean
	@ConditionalOnMissingBean({ WebDriver.class, MockMvcHtmlUnitDriverBuilder.class })
	@ConditionalOnBean(MockMvc.class)
	public MockMvcHtmlUnitDriverBuilder mockMvcHtmlUnitDriverBuilder(MockMvc mockMvc) {
		return MockMvcHtmlUnitDriverBuilder.mockMvcSetup(mockMvc)
				.withDelegate(new LocalHostWebConnectionHtmlUnitDriver(this.environment,
						BrowserVersion.CHROME));
	}

	@Bean
	@ConditionalOnMissingBean(WebDriver.class)
	@ConditionalOnBean(MockMvcHtmlUnitDriverBuilder.class)
	public HtmlUnitDriver htmlUnitDriver(MockMvcHtmlUnitDriverBuilder builder) {
		return builder.build();
	}

}
