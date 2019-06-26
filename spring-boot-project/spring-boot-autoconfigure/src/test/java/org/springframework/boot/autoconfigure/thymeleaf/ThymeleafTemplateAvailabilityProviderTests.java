

package org.springframework.boot.autoconfigure.thymeleaf;

import org.junit.Test;

import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ThymeleafTemplateAvailabilityProvider}.
 *
 * @author Andy Wilkinson
 */
public class ThymeleafTemplateAvailabilityProviderTests {

	private final TemplateAvailabilityProvider provider = new ThymeleafTemplateAvailabilityProvider();

	private final ResourceLoader resourceLoader = new DefaultResourceLoader();

	private final MockEnvironment environment = new MockEnvironment();

	@Test
	public void availabilityOfTemplateInDefaultLocation() {
		assertThat(this.provider.isTemplateAvailable("home", this.environment,
				getClass().getClassLoader(), this.resourceLoader)).isTrue();
	}

	@Test
	public void availabilityOfTemplateThatDoesNotExist() {
		assertThat(this.provider.isTemplateAvailable("whatever", this.environment,
				getClass().getClassLoader(), this.resourceLoader)).isFalse();
	}

	@Test
	public void availabilityOfTemplateWithCustomPrefix() {
		this.environment.setProperty("spring.thymeleaf.prefix",
				"classpath:/custom-templates/");
		assertThat(this.provider.isTemplateAvailable("custom", this.environment,
				getClass().getClassLoader(), this.resourceLoader)).isTrue();
	}

	@Test
	public void availabilityOfTemplateWithCustomSuffix() {
		this.environment.setProperty("spring.thymeleaf.suffix", ".thymeleaf");
		assertThat(this.provider.isTemplateAvailable("suffixed", this.environment,
				getClass().getClassLoader(), this.resourceLoader)).isTrue();
	}

}
