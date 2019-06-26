

package org.springframework.boot.autoconfigure.condition;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.condition.OnBeanCondition.BeanTypeDeductionException;
import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests for {@link OnBeanCondition} when deduction of the bean's type fails
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("jackson-core-*.jar")
public class OnBeanConditionTypeDeductionFailureTests {

	@Test
	public void conditionalOnMissingBeanWithDeducedTypeThatIsPartiallyMissingFromClassPath() {
		try {
			new AnnotationConfigApplicationContext(ImportingConfiguration.class).close();
			fail("Context refresh was successful");
		}
		catch (Exception ex) {
			Throwable beanTypeDeductionException = findNestedCause(ex,
					BeanTypeDeductionException.class);
			assertThat(beanTypeDeductionException)
					.hasMessage("Failed to deduce bean type for "
							+ OnMissingBeanConfiguration.class.getName()
							+ ".objectMapper");
			assertThat(findNestedCause(beanTypeDeductionException,
					NoClassDefFoundError.class)).isNotNull();

		}
	}

	private Throwable findNestedCause(Throwable ex, Class<? extends Throwable> target) {
		Throwable candidate = ex;
		while (candidate != null) {
			if (target.isInstance(candidate)) {
				return candidate;
			}
			candidate = candidate.getCause();
		}
		return null;
	}

	@Configuration
	@Import(OnMissingBeanImportSelector.class)
	static class ImportingConfiguration {

	}

	@Configuration
	static class OnMissingBeanConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public ObjectMapper objectMapper() {
			return new ObjectMapper();
		}

	}

	static class OnMissingBeanImportSelector implements ImportSelector {

		@Override
		public String[] selectImports(AnnotationMetadata importingClassMetadata) {
			return new String[] { OnMissingBeanConfiguration.class.getName() };
		}

	}

}
