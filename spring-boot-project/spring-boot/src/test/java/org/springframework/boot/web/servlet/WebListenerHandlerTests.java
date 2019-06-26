

package org.springframework.boot.web.servlet;

import java.io.IOException;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

import org.junit.Test;

import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

/**
 * Tests for {@link WebListenerHandler}.
 *
 * @author Andy Wilkinson
 */
public class WebListenerHandlerTests {

	private final WebListenerHandler handler = new WebListenerHandler();

	private final SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

	@Test
	public void listener() throws IOException {
		ScannedGenericBeanDefinition scanned = new ScannedGenericBeanDefinition(
				new SimpleMetadataReaderFactory()
						.getMetadataReader(TestListener.class.getName()));
		this.handler.handle(scanned, this.registry);
		this.registry.getBeanDefinition(TestListener.class.getName());
	}

	@WebListener
	static class TestListener implements ServletContextAttributeListener {

		@Override
		public void attributeAdded(ServletContextAttributeEvent event) {

		}

		@Override
		public void attributeRemoved(ServletContextAttributeEvent event) {

		}

		@Override
		public void attributeReplaced(ServletContextAttributeEvent event) {

		}

	}

}
