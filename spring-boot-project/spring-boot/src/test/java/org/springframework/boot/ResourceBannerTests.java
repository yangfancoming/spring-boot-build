

package org.springframework.boot;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ResourceBanner}.
 *
 * @author Phillip Webb
 * @author Vedran Pavic
 */
public class ResourceBannerTests {

	@After
	public void reset() {
		AnsiOutput.setEnabled(Enabled.DETECT);
	}

	@Test
	public void renderVersions() {
		Resource resource = new ByteArrayResource(
				"banner ${a} ${spring-boot.version} ${application.version}".getBytes());
		String banner = printBanner(resource, "10.2", "2.0", null);
		assertThat(banner).startsWith("banner 1 10.2 2.0");
	}

	@Test
	public void renderWithoutVersions() {
		Resource resource = new ByteArrayResource(
				"banner ${a} ${spring-boot.version} ${application.version}".getBytes());
		String banner = printBanner(resource, null, null, null);
		assertThat(banner).startsWith("banner 1  ");
	}

	@Test
	public void renderFormattedVersions() {
		Resource resource = new ByteArrayResource(
				"banner ${a}${spring-boot.formatted-version}${application.formatted-version}"
						.getBytes());
		String banner = printBanner(resource, "10.2", "2.0", null);
		assertThat(banner).startsWith("banner 1 (v10.2) (v2.0)");
	}

	@Test
	public void renderWithoutFormattedVersions() {
		Resource resource = new ByteArrayResource(
				"banner ${a}${spring-boot.formatted-version}${application.formatted-version}"
						.getBytes());
		String banner = printBanner(resource, null, null, null);
		assertThat(banner).startsWith("banner 1");
	}

	@Test
	public void renderWithColors() {
		Resource resource = new ByteArrayResource(
				"${Ansi.RED}This is red.${Ansi.NORMAL}".getBytes());
		AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
		String banner = printBanner(resource, null, null, null);
		assertThat(banner).startsWith("\u001B[31mThis is red.\u001B[0m");
	}

	@Test
	public void renderWithColorsButDisabled() {
		Resource resource = new ByteArrayResource(
				"${Ansi.RED}This is red.${Ansi.NORMAL}".getBytes());
		AnsiOutput.setEnabled(AnsiOutput.Enabled.NEVER);
		String banner = printBanner(resource, null, null, null);
		assertThat(banner).startsWith("This is red.");
	}

	@Test
	public void renderWithTitle() {
		Resource resource = new ByteArrayResource(
				"banner ${application.title} ${a}".getBytes());
		String banner = printBanner(resource, null, null, "title");
		assertThat(banner).startsWith("banner title 1");
	}

	@Test
	public void renderWithoutTitle() {
		Resource resource = new ByteArrayResource(
				"banner ${application.title} ${a}".getBytes());
		String banner = printBanner(resource, null, null, null);
		assertThat(banner).startsWith("banner  1");
	}

	private String printBanner(Resource resource, String bootVersion,
			String applicationVersion, String applicationTitle) {
		ResourceBanner banner = new MockResourceBanner(resource, bootVersion,
				applicationVersion, applicationTitle);
		ConfigurableEnvironment environment = new MockEnvironment();
		Map<String, Object> source = Collections.singletonMap("a", "1");
		environment.getPropertySources().addLast(new MapPropertySource("map", source));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		banner.printBanner(environment, getClass(), new PrintStream(out));
		return out.toString();
	}

	private static class MockResourceBanner extends ResourceBanner {

		private final String bootVersion;

		private final String applicationVersion;

		private final String applicationTitle;

		MockResourceBanner(Resource resource, String bootVersion,
				String applicationVersion, String applicationTitle) {
			super(resource);
			this.bootVersion = bootVersion;
			this.applicationVersion = applicationVersion;
			this.applicationTitle = applicationTitle;
		}

		@Override
		protected String getBootVersion() {
			return this.bootVersion;
		}

		@Override
		protected String getApplicationVersion(Class<?> sourceClass) {
			return this.applicationVersion;
		}

		@Override
		protected String getApplicationTitle(Class<?> sourceClass) {
			return this.applicationTitle;
		}

	}

}
