

package org.springframework.boot.cli.command;

import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.cli.command.run.RunCommand;
import org.springframework.boot.test.rule.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 */
public class CommandRunnerIntegrationTests {

	@Rule
	public OutputCapture output = new OutputCapture();

	@Test
	public void debugAddsAutoconfigReport() {
		CommandRunner runner = new CommandRunner("spring");
		runner.addCommand(new RunCommand());
		// -d counts as "debug" for the spring command, but not for the
		// LoggingApplicationListener
		runner.runAndHandleErrors("run", "samples/app.groovy", "-d");
		assertThat(this.output.toString()).contains("Negative matches:");
	}

	@Test
	public void debugSwitchedOffForAppArgs() {
		CommandRunner runner = new CommandRunner("spring");
		runner.addCommand(new RunCommand());
		runner.runAndHandleErrors("run", "samples/app.groovy", "--", "-d");
		assertThat(this.output.toString()).doesNotContain("Negative matches:");
	}

}
