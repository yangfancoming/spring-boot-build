

package org.springframework.boot.devtools.autoconfigure;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DevToolsProperties}.
 *
 * @author Stephane Nicoll
 */
public class DevToolsPropertiesTests {

	private final DevToolsProperties devToolsProperties = new DevToolsProperties();

	@Test
	public void additionalExcludeKeepsDefaults() {
		DevToolsProperties.Restart restart = this.devToolsProperties.getRestart();
		restart.setAdditionalExclude("foo/**,bar/**");
		assertThat(restart.getAllExclude()).containsOnly("META-INF/maven/**",
				"META-INF/resources/**", "resources/**", "static/**", "public/**",
				"templates/**", "**/*Test.class", "**/*Tests.class", "git.properties",
				"META-INF/build-info.properties", "foo/**", "bar/**");
	}

	@Test
	public void additionalExcludeNoDefault() {
		DevToolsProperties.Restart restart = this.devToolsProperties.getRestart();
		restart.setExclude("");
		restart.setAdditionalExclude("foo/**,bar/**");
		assertThat(restart.getAllExclude()).containsOnly("foo/**", "bar/**");
	}

	@Test
	public void additionalExcludeCustomDefault() {
		DevToolsProperties.Restart restart = this.devToolsProperties.getRestart();
		restart.setExclude("biz/**");
		restart.setAdditionalExclude("foo/**,bar/**");
		assertThat(restart.getAllExclude()).containsOnly("biz/**", "foo/**", "bar/**");
	}

}
