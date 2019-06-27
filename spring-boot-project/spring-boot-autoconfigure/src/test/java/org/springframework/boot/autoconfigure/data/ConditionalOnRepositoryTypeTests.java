

package org.springframework.boot.autoconfigure.data;

import org.junit.Test;

import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConditionalOnRepositoryType}.
 *
 * @author Andy Wilkinson
 */
public class ConditionalOnRepositoryTypeTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	public void imperativeRepositoryMatchesWithNoConfiguredType() {
		this.contextRunner.withUserConfiguration(ImperativeRepository.class)
				.run((context) -> assertThat(context)
						.hasSingleBean(ImperativeRepository.class));
	}

	@Test
	public void reactiveRepositoryMatchesWithNoConfiguredType() {
		this.contextRunner.withUserConfiguration(ReactiveRepository.class).run(
				(context) -> assertThat(context).hasSingleBean(ReactiveRepository.class));
	}

	@Test
	public void imperativeRepositoryMatchesWithAutoConfiguredType() {
		this.contextRunner.withUserConfiguration(ImperativeRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:auto")
				.run((context) -> assertThat(context)
						.hasSingleBean(ImperativeRepository.class));
	}

	@Test
	public void reactiveRepositoryMatchesWithAutoConfiguredType() {
		this.contextRunner.withUserConfiguration(ReactiveRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:auto")
				.run((context) -> assertThat(context)
						.hasSingleBean(ReactiveRepository.class));
	}

	@Test
	public void imperativeRepositoryMatchesWithImperativeConfiguredType() {
		this.contextRunner.withUserConfiguration(ImperativeRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:imperative")
				.run((context) -> assertThat(context)
						.hasSingleBean(ImperativeRepository.class));
	}

	@Test
	public void reactiveRepositoryMatchesWithReactiveConfiguredType() {
		this.contextRunner.withUserConfiguration(ReactiveRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:reactive")
				.run((context) -> assertThat(context)
						.hasSingleBean(ReactiveRepository.class));
	}

	@Test
	public void imperativeRepositoryDoesNotMatchWithReactiveConfiguredType() {
		this.contextRunner.withUserConfiguration(ImperativeRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:reactive")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ImperativeRepository.class));
	}

	@Test
	public void reactiveRepositoryDoesNotMatchWithImperativeConfiguredType() {
		this.contextRunner.withUserConfiguration(ReactiveRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:imperative")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ReactiveRepository.class));
	}

	@Test
	public void imperativeRepositoryDoesNotMatchWithNoneConfiguredType() {
		this.contextRunner.withUserConfiguration(ImperativeRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:none")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ImperativeRepository.class));
	}

	@Test
	public void reactiveRepositoryDoesNotMatchWithNoneConfiguredType() {
		this.contextRunner.withUserConfiguration(ReactiveRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:none")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(ReactiveRepository.class));
	}

	@Test
	public void failsFastWhenConfiguredTypeIsUnknown() {
		this.contextRunner.withUserConfiguration(ReactiveRepository.class)
				.withPropertyValues("spring.data.test.repositories.type:abcde")
				.run((context) -> assertThat(context).hasFailed());
	}

	@Configuration
	@ConditionalOnRepositoryType(store = "test", type = RepositoryType.IMPERATIVE)
	protected static class ImperativeRepository {

	}

	@Configuration
	@ConditionalOnRepositoryType(store = "test", type = RepositoryType.REACTIVE)
	protected static class ReactiveRepository {

	}

}
