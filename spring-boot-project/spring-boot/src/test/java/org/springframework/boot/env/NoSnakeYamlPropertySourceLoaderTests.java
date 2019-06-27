

package org.springframework.boot.env;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.core.io.ByteArrayResource;

/**
 * Tests for {@link YamlPropertySourceLoader} when snakeyaml is not available.
 *
 * @author Madhura Bhave
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("snakeyaml-*.jar")
public class NoSnakeYamlPropertySourceLoaderTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

	@Test
	public void load() throws Exception {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage(
				"Attempted to load resource but snakeyaml was not found on the classpath");
		ByteArrayResource resource = new ByteArrayResource(
				"foo:\n  bar: spam".getBytes());
		this.loader.load("resource", resource);
	}

}
