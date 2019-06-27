

package org.springframework.boot.actuate.endpoint.web.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * A custom {@link Runner} that tests web endpoints that are made available over HTTP
 * using Jersey, Spring MVC, and WebFlux.
 * <p>
 * The following types can be automatically injected into static fields on the test class:
 * <ul>
 * <li>{@link WebTestClient}</li>
 * <li>{@link ConfigurableApplicationContext}</li>
 * </ul>
 * <p>
 * The {@link PropertySource PropertySources} that belong to the application context's
 * {@link org.springframework.core.env.Environment} are reset at the end of every test.
 * This means that {@link TestPropertyValues} can be used in a test without affecting the
 * {@code Environment} of other tests in the same class. The runner always sets the flag
 * {@code management.endpoints.web.exposure.include} to {@code *} so that web endpoints
 * are enabled.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 */
public class WebEndpointRunners extends Suite {

	public WebEndpointRunners(Class<?> testClass) throws InitializationError {
		super(testClass, createRunners(testClass));
	}

	private static List<Runner> createRunners(Class<?> testClass)
			throws InitializationError {
		List<Runner> runners = new ArrayList<>();
		runners.add(new WebFluxEndpointsRunner(testClass));
		runners.add(new WebMvcEndpointRunner(testClass));
		runners.add(new JerseyEndpointsRunner(testClass));
		return runners;
	}

}
