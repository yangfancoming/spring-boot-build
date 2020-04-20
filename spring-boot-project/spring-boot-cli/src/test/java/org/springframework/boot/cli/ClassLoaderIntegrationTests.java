

package org.springframework.boot.cli;

import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for CLI Classloader issues.
 *
 * @author Phillip Webb
 */
public class ClassLoaderIntegrationTests {

	@Rule
	public CliTester cli = new CliTester("src/test/resources/");

	@Test
	public void runWithIsolatedClassLoader() throws Exception {
		// CLI classes or dependencies should not be exposed to the app
		String output = this.cli.run("classloader-test-app.groovy",
				SpringCli.class.getName());
		assertThat(output).contains("HasClasses-false-true-false");
	}

}
