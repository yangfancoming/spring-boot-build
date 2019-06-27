

package org.springframework.boot.test.web.reactive.server;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.util.ClassUtils;

/**
 * {@link ContextCustomizerFactory} for {@code WebTestClient}.
 *
 * @author Stephane Nicoll
 */
class WebTestClientContextCustomizerFactory implements ContextCustomizerFactory {

	private static final String WEB_TEST_CLIENT_CLASS = "org.springframework.web.reactive.function.client.WebClient";

	@Override
	public ContextCustomizer createContextCustomizer(Class<?> testClass,
			List<ContextConfigurationAttributes> configAttributes) {
		if (isWebClientPresent() && AnnotatedElementUtils.findMergedAnnotation(testClass,
				SpringBootTest.class) != null) {
			return new WebTestClientContextCustomizer();
		}
		return null;
	}

	private boolean isWebClientPresent() {
		return ClassUtils.isPresent(WEB_TEST_CLIENT_CLASS, getClass().getClassLoader());
	}

}
