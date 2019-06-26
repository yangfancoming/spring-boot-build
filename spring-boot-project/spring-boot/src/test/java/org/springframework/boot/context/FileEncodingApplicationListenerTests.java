

package org.springframework.boot.context;

import org.junit.Assume;
import org.junit.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.support.TestPropertySourceUtils;

/**
 * Tests for {@link FileEncodingApplicationListener}.
 *
 * @author Dave Syer
 */
public class FileEncodingApplicationListenerTests {

	private final FileEncodingApplicationListener initializer = new FileEncodingApplicationListener();

	private final ConfigurableEnvironment environment = new StandardEnvironment();

	private final ApplicationEnvironmentPreparedEvent event = new ApplicationEnvironmentPreparedEvent(
			new SpringApplication(), new String[0], this.environment);

	@Test(expected = IllegalStateException.class)
	public void testIllegalState() {
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(this.environment,
				"spring.mandatory_file_encoding=FOO");
		ConfigurationPropertySources.attach(this.environment);
		this.initializer.onApplicationEvent(this.event);
	}

	@Test
	public void testSunnyDayNothingMandated() {
		this.initializer.onApplicationEvent(this.event);
	}

	@Test
	public void testSunnyDayMandated() {
		Assume.assumeNotNull(System.getProperty("file.encoding"));
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(this.environment,
				"spring.mandatory_file_encoding:" + System.getProperty("file.encoding"));
		ConfigurationPropertySources.attach(this.environment);
		this.initializer.onApplicationEvent(this.event);
	}

}
