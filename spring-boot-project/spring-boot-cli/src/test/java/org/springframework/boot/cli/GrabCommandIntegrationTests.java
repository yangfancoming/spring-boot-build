

package org.springframework.boot.cli;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.cli.command.grab.GrabCommand;
import org.springframework.util.FileSystemUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Integration tests for {@link GrabCommand}
 *
 * @author Andy Wilkinson
 * @author Dave Syer
 */
public class GrabCommandIntegrationTests {

	@Rule
	public CliTester cli = new CliTester("src/test/resources/grab-samples/");

	@Before
	@After
	public void deleteLocalRepository() {
		FileSystemUtils.deleteRecursively(new File("target/repository"));
		System.clearProperty("grape.root");
		System.clearProperty("groovy.grape.report.downloads");
	}

	@Test
	public void grab() throws Exception {

		System.setProperty("grape.root", "target");
		System.setProperty("groovy.grape.report.downloads", "true");

		// Use --autoconfigure=false to limit the amount of downloaded dependencies
		String output = this.cli.grab("grab.groovy", "--autoconfigure=false");
		assertThat(new File("target/repository/joda-time/joda-time").isDirectory())
				.isTrue();
		// Should be resolved from local repository cache
		assertThat(output.contains("Downloading: file:")).isTrue();
	}

	@Test
	public void duplicateDependencyManagementBomAnnotationsProducesAnError() {
		try {
			this.cli.grab("duplicateDependencyManagementBom.groovy");
			fail();
		}
		catch (Exception ex) {
			assertThat(ex.getMessage())
					.contains("Duplicate @DependencyManagementBom annotation");
		}
	}

	@Test
	public void customMetadata() throws Exception {
		System.setProperty("grape.root", "target");
		FileSystemUtils.copyRecursively(
				new File("src/test/resources/grab-samples/repository"),
				new File("target/repository"));
		this.cli.grab("customDependencyManagement.groovy", "--autoconfigure=false");
		assertThat(new File("target/repository/javax/ejb/ejb-api/3.0").isDirectory())
				.isTrue();
	}

}
