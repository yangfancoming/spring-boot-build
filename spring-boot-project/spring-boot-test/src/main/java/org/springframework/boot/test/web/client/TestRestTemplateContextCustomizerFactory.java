

package org.springframework.boot.test.web.client;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

/**
 * {@link ContextCustomizerFactory} for {@link TestRestTemplate}.
 *
 * @author Andy Wilkinson
 * @see TestRestTemplateContextCustomizer
 */
class TestRestTemplateContextCustomizerFactory implements ContextCustomizerFactory {

	@Override
	public ContextCustomizer createContextCustomizer(Class<?> testClass,
			List<ContextConfigurationAttributes> configAttributes) {
		if (AnnotatedElementUtils.findMergedAnnotation(testClass,
				SpringBootTest.class) != null) {
			return new TestRestTemplateContextCustomizer();
		}
		return null;
	}

}
