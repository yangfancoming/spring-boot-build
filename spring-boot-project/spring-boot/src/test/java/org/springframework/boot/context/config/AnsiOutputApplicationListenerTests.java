

package org.springframework.boot.context.config;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.boot.ansi.AnsiOutputEnabledValue;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AnsiOutputApplicationListener}.
 *
 * @author Phillip Webb
 */
public class AnsiOutputApplicationListenerTests {

	private ConfigurableApplicationContext context;

	@Before
	public void resetAnsi() {
		AnsiOutput.setEnabled(Enabled.DETECT);
	}

	@After
	public void cleanUp() {
		resetAnsi();
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void enabled() {
		SpringApplication application = new SpringApplication(Config.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		Map<String, Object> props = new HashMap<>();
		props.put("spring.output.ansi.enabled", "ALWAYS");
		application.setDefaultProperties(props);
		this.context = application.run();
		assertThat(AnsiOutputEnabledValue.get()).isEqualTo(Enabled.ALWAYS);
	}

	@Test
	public void disabled() {
		SpringApplication application = new SpringApplication(Config.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		Map<String, Object> props = new HashMap<>();
		props.put("spring.output.ansi.enabled", "never");
		application.setDefaultProperties(props);
		this.context = application.run();
		assertThat(AnsiOutputEnabledValue.get()).isEqualTo(Enabled.NEVER);
	}

	@Test
	public void disabledViaApplicationProperties() {
		ConfigurableEnvironment environment = new StandardEnvironment();
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,
				"spring.config.name=ansi");
		SpringApplication application = new SpringApplication(Config.class);
		application.setEnvironment(environment);
		application.setWebApplicationType(WebApplicationType.NONE);
		this.context = application.run();
		assertThat(AnsiOutputEnabledValue.get()).isEqualTo(Enabled.NEVER);
	}

	@Configuration
	public static class Config {

	}

}
