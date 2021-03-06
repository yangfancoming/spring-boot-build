

package org.springframework.boot.test.context.runner;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.boot.context.annotation.Configurations;
import org.springframework.boot.test.context.assertj.AssertableWebApplicationContext;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * An {@link AbstractApplicationContextRunner ApplicationContext runner} for a Servlet based {@link ConfigurableWebApplicationContext}.
 * See {@link AbstractApplicationContextRunner} for details.
 * @since 2.0.0
 */
public final class WebApplicationContextRunner extends AbstractApplicationContextRunner<WebApplicationContextRunner, ConfigurableWebApplicationContext, AssertableWebApplicationContext> {

	/**
	 * Create a new {@link WebApplicationContextRunner} instance using an {@link AnnotationConfigWebApplicationContext} with a {@link MockServletContext} as the underlying source.
	 * @see #withMockServletContext(Supplier)
	 */
	public WebApplicationContextRunner() {
		this(withMockServletContext(AnnotationConfigWebApplicationContext::new));
	}

	/**
	 * Create a new {@link WebApplicationContextRunner} instance using the specified {@code contextFactory} as the underlying source.
	 * @param contextFactory a supplier that returns a new instance on each call
	 */
	public WebApplicationContextRunner(Supplier<ConfigurableWebApplicationContext> contextFactory) {
		super(contextFactory);
	}

	private WebApplicationContextRunner(
			Supplier<ConfigurableWebApplicationContext> contextFactory,
			List<ApplicationContextInitializer<ConfigurableWebApplicationContext>> initializers,
			TestPropertyValues environmentProperties, TestPropertyValues systemProperties,
			ClassLoader classLoader, ApplicationContext parent,
			List<Configurations> configurations) {
		super(contextFactory, initializers, environmentProperties, systemProperties,classLoader, parent, configurations);
	}

	@Override
	protected WebApplicationContextRunner newInstance(
			Supplier<ConfigurableWebApplicationContext> contextFactory,
			List<ApplicationContextInitializer<ConfigurableWebApplicationContext>> initializers,
			TestPropertyValues environmentProperties, TestPropertyValues systemProperties,
			ClassLoader classLoader, ApplicationContext parent,
			List<Configurations> configurations) {
		return new WebApplicationContextRunner(contextFactory, initializers,environmentProperties, systemProperties, classLoader, parent,configurations);
	}

	/**
	 * Decorate the specified {@code contextFactory} to set a {@link MockServletContext}
	 * on each newly created {@link WebApplicationContext}.
	 * @param contextFactory the context factory to decorate
	 * @return an updated supplier that will set the {@link MockServletContext}
	 */
	public static Supplier<ConfigurableWebApplicationContext> withMockServletContext(Supplier<ConfigurableWebApplicationContext> contextFactory) {
		return (contextFactory != null) ? () -> {
			ConfigurableWebApplicationContext context = contextFactory.get();
			context.setServletContext(new MockServletContext());
			return context;
		} : null;
	}

}
