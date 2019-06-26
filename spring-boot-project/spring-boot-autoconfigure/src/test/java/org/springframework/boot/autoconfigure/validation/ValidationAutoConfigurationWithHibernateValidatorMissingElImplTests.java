

package org.springframework.boot.autoconfigure.validation;

import javax.validation.Validator;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.testsupport.runner.classpath.ClassPathExclusions;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link ValidationAutoConfiguration} when Hibernate validator is present but no
 * EL implementation is available.
 *
 * @author Stephane Nicoll
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions({ "tomcat-embed-el-*.jar", "el-api-*.jar" })
public class ValidationAutoConfigurationWithHibernateValidatorMissingElImplTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void missingElDependencyIsTolerated() {
		this.context = new AnnotationConfigApplicationContext(
				ValidationAutoConfiguration.class);
		assertThat(this.context.getBeansOfType(Validator.class)).hasSize(1);
		assertThat(this.context.getBeansOfType(MethodValidationPostProcessor.class))
				.hasSize(1);
	}

}
