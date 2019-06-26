

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootTest} with active profiles. See gh-1469.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest("spring.config.name=enableother")
@ActiveProfiles("override")
public class SpringBootTestActiveProfileTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void profiles() {
		assertThat(this.context.getEnvironment().getActiveProfiles())
				.containsExactly("override");
	}

	@Configuration
	protected static class Config {

	}

}
