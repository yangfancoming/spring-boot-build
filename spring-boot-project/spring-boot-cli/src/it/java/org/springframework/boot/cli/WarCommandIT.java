

package org.springframework.boot.cli;

import java.io.File;

import org.junit.Test;

import org.springframework.boot.cli.command.archive.WarCommand;
import org.springframework.boot.cli.infrastructure.CommandLineInvoker;
import org.springframework.boot.cli.infrastructure.CommandLineInvoker.Invocation;
import org.springframework.boot.loader.tools.JavaExecutable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link WarCommand}.
 *
 * @author Andrey Stolyarov
 * @author Henri Kerola
 */
public class WarCommandIT {

	private final CommandLineInvoker cli = new CommandLineInvoker(
			new File("src/it/resources/war-command"));

	@Test
	public void warCreation() throws Exception {
		File war = new File("target/test-app.war");
		Invocation invocation = this.cli.invoke("war", war.getAbsolutePath(),
				"war.groovy");
		invocation.await();
		assertThat(war.exists()).isTrue();
		Process process = new JavaExecutable()
				.processBuilder("-jar", war.getAbsolutePath(), "--server.port=0").start();
		invocation = new Invocation(process);
		invocation.await();
		assertThat(invocation.getOutput()).contains("onStart error");
		assertThat(invocation.getOutput()).contains("Tomcat started");
		assertThat(invocation.getOutput())
				.contains("/WEB-INF/lib-provided/tomcat-embed-core");
		assertThat(invocation.getOutput()).contains("WEB-INF/classes!/root.properties");
		process.destroy();
	}

}
