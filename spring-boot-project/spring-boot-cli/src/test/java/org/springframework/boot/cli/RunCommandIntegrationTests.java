

package org.springframework.boot.cli;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.cli.command.run.RunCommand;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link RunCommand}.
 *
 * @author Andy Wilkinson
 */
public class RunCommandIntegrationTests {

	@Rule
	public CliTester cli = new CliTester("src/it/resources/run-command/");

	private Properties systemProperties = new Properties();

	@Before
	public void captureSystemProperties() {
		this.systemProperties.putAll(System.getProperties());
	}

	@After
	public void restoreSystemProperties() {
		System.setProperties(this.systemProperties);
	}

	@Test
	public void bannerAndLoggingIsOutputByDefault() throws Exception {
		String output = this.cli.run("quiet.groovy");
		assertThat(output).contains(" :: Spring Boot ::");
		assertThat(output).contains("Starting application");
		assertThat(output).contains("Ssshh");
	}

	@Test
	public void quietModeSuppressesAllCliOutput() throws Exception {
		this.cli.run("quiet.groovy");
		String output = this.cli.run("quiet.groovy", "-q");
		assertThat(output).isEqualTo("Ssshh");
	}

}
