

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
 * Test for {@link ValidationAutoConfiguration} when no JSR-303 provider is available.
 *
 * @author Stephane Nicoll
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathExclusions("hibernate-validator-*.jar")
public class ValidationAutoConfigurationWithoutValidatorTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void validationIsDisabled() {
		this.context = new AnnotationConfigApplicationContext(
				ValidationAutoConfiguration.class);
		assertThat(this.context.getBeansOfType(Validator.class)).isEmpty();
		assertThat(this.context.getBeansOfType(MethodValidationPostProcessor.class))
				.isEmpty();
	}

}
