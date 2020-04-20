

package org.springframework.boot.cli.compiler.dependencies;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Tests for {@link SpringBootDependenciesDependencyManagement}
 *
 * @author Andy Wilkinson
 */
public class SpringBootDependenciesDependencyManagementTests {

	private final DependencyManagement dependencyManagement = new SpringBootDependenciesDependencyManagement();

	@Test
	public void springBootVersion() {
		assertThat(this.dependencyManagement.getSpringBootVersion()).isNotNull();
	}

	@Test
	public void find() {
		Dependency dependency = this.dependencyManagement.find("spring-boot");
		assertThat(dependency).isNotNull();
		assertThat(dependency.getGroupId()).isEqualTo("org.springframework.boot");
		assertThat(dependency.getArtifactId()).isEqualTo("spring-boot");
	}

	@Test
	public void getDependencies() {
		assertThat(this.dependencyManagement.getDependencies()).isNotEqualTo(empty());
	}

}
