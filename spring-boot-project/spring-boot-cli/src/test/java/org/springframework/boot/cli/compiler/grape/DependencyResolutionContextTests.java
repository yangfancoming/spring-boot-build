

package org.springframework.boot.cli.compiler.grape;

import org.junit.Test;

import org.springframework.boot.cli.compiler.dependencies.SpringBootDependenciesDependencyManagement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 *
 */
public class DependencyResolutionContextTests {

	@Test
	public void defaultDependenciesEmpty() {
		assertThat(new DependencyResolutionContext().getManagedDependencies()).isEmpty();
	}

	@Test
	public void canAddSpringBootDependencies() {
		DependencyResolutionContext dependencyResolutionContext = new DependencyResolutionContext();
		dependencyResolutionContext.addDependencyManagement(
				new SpringBootDependenciesDependencyManagement());
		assertThat(dependencyResolutionContext.getManagedDependencies()).isNotEmpty();
	}

}
