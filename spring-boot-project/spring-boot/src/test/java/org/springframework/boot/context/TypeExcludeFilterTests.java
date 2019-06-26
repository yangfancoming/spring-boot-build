

package org.springframework.boot.context;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.filtersample.ExampleComponent;
import org.springframework.boot.context.filtersample.ExampleFilteredComponent;
import org.springframework.boot.context.filtersample.SampleTypeExcludeFilter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TypeExcludeFilter}.
 *
 * @author Phillip Webb
 */
public class TypeExcludeFilterTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private AnnotationConfigApplicationContext context;

	@After
	public void cleanUp() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void loadsTypeExcludeFilters() {
		this.context = new AnnotationConfigApplicationContext();
		this.context.getBeanFactory().registerSingleton("filter1",
				new WithoutMatchOverrideFilter());
		this.context.getBeanFactory().registerSingleton("filter2",
				new SampleTypeExcludeFilter());
		this.context.register(Config.class);
		this.context.refresh();
		assertThat(this.context.getBean(ExampleComponent.class)).isNotNull();
		this.thrown.expect(NoSuchBeanDefinitionException.class);
		this.context.getBean(ExampleFilteredComponent.class);
	}

	@Configuration
	@ComponentScan(basePackageClasses = SampleTypeExcludeFilter.class, excludeFilters = @Filter(type = FilterType.CUSTOM, classes = SampleTypeExcludeFilter.class))
	static class Config {

	}

	static class WithoutMatchOverrideFilter extends TypeExcludeFilter {

	}

}
