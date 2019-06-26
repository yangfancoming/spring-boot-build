

package org.springframework.boot.validation;

import javax.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MessageInterpolatorFactory}.
 *
 * @author Phillip Webb
 */
public class MessageInterpolatorFactoryTests {

	@Test
	public void getObjectShouldReturnResourceBundleMessageInterpolator() {
		MessageInterpolator interpolator = new MessageInterpolatorFactory().getObject();
		assertThat(interpolator).isInstanceOf(ResourceBundleMessageInterpolator.class);
	}

}
