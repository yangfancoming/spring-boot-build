

package org.springframework.boot;

import org.junit.Test;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link EnvironmentConverter}.
 *
 * @author Ethan Rubinson
 * @author Andy Wilkinson
 */
public class EnvironmentConverterTests {

	private final EnvironmentConverter environmentConverter = new EnvironmentConverter(
			getClass().getClassLoader());

	@Test
	public void convertedEnvironmentHasSameActiveProfiles() {
		AbstractEnvironment originalEnvironment = new MockEnvironment();
		originalEnvironment.setActiveProfiles("activeProfile1", "activeProfile2");
		StandardEnvironment convertedEnvironment = this.environmentConverter
				.convertToStandardEnvironmentIfNecessary(originalEnvironment);
		assertThat(convertedEnvironment.getActiveProfiles())
				.containsExactly("activeProfile1", "activeProfile2");
	}

	@Test
	public void convertedEnvironmentHasSameConversionService() {
		AbstractEnvironment originalEnvironment = new MockEnvironment();
		ConfigurableConversionService conversionService = mock(
				ConfigurableConversionService.class);
		originalEnvironment.setConversionService(conversionService);
		StandardEnvironment convertedEnvironment = this.environmentConverter
				.convertToStandardEnvironmentIfNecessary(originalEnvironment);
		assertThat(convertedEnvironment.getConversionService())
				.isEqualTo(conversionService);
	}

	@Test
	public void standardEnvironmentIsReturnedUnconverted() {
		StandardEnvironment standardEnvironment = new StandardEnvironment();
		StandardEnvironment convertedEnvironment = this.environmentConverter
				.convertToStandardEnvironmentIfNecessary(standardEnvironment);
		assertThat(convertedEnvironment).isSameAs(standardEnvironment);
	}

	@Test
	public void standardServletEnvironmentIsConverted() {
		StandardServletEnvironment standardServletEnvironment = new StandardServletEnvironment();
		StandardEnvironment convertedEnvironment = this.environmentConverter
				.convertToStandardEnvironmentIfNecessary(standardServletEnvironment);
		assertThat(convertedEnvironment).isNotSameAs(standardServletEnvironment);
	}

}
