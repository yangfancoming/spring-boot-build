

package org.springframework.boot.cli;

import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for code in directories.
 *
 * @author Dave Syer
 */
public class DirectorySourcesIntegrationTests {

	@Rule
	public CliTester cli = new CliTester("src/test/resources/dir-sample/");

	@Test
	public void runDirectory() throws Exception {
		assertThat(this.cli.run("code")).contains("Hello World");
	}

	@Test
	public void runDirectoryRecursive() throws Exception {
		assertThat(this.cli.run("")).contains("Hello World");
	}

	@Test
	public void runPathPattern() throws Exception {
		assertThat(this.cli.run("**/*.groovy")).contains("Hello World");
	}

}
