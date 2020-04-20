

package org.springframework.boot.cli;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests to exercise and reproduce specific issues.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class ReproIntegrationTests {

	@Rule
	public CliTester cli = new CliTester("src/test/resources/repro-samples/");

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void grabAntBuilder() throws Exception {
		this.cli.run("grab-ant-builder.groovy");
		assertThat(this.cli.getHttpOutput()).contains("{\"message\":\"Hello World\"}");
	}

	// Security depends on old versions of Spring so if the dependencies aren't pinned
	// this will fail
	@Test
	public void securityDependencies() throws Exception {
		assertThat(this.cli.run("secure.groovy")).contains("Hello World");
	}

	@Test
	public void dataJpaDependencies() throws Exception {
		assertThat(this.cli.run("data-jpa.groovy")).contains("Hello World");
	}

	@Test
	public void jarFileExtensionNeeded() throws Exception {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("is not a JAR file");
		this.cli.jar("secure.groovy", "data-jpa.groovy");
	}

}
