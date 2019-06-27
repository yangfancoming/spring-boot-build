

package org.springframework.boot.test.context.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

/**
 * {@link ContextCustomizer} to add the {@link TestTypeExcludeFilter} to the
 * {@link ApplicationContext}.
 *
 * @author Phillip Webb
 */
class ExcludeFilterContextCustomizer implements ContextCustomizer {

	@Override
	public void customizeContext(ConfigurableApplicationContext context,
			MergedContextConfiguration mergedContextConfiguration) {
		context.getBeanFactory().registerSingleton(TestTypeExcludeFilter.class.getName(),
				new TestTypeExcludeFilter());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
