

package org.springframework.boot.actuate.endpoint.web.annotation;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.DiscoveredEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.EndpointServlet;
import org.springframework.boot.actuate.endpoint.web.ExposableServletEndpoint;
import org.springframework.boot.actuate.endpoint.web.PathMapper;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ContextConsumer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ServletEndpointDiscoverer}.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 */
public class ServletEndpointDiscovererTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	public void getEndpointsWhenNoEndpointBeansShouldReturnEmptyCollection() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
				.run(assertDiscoverer(
						(discoverer) -> assertThat(discoverer.getEndpoints()).isEmpty()));
	}

	@Test
	public void getEndpointsShouldIncludeServletEndpoints() {
		this.contextRunner.withUserConfiguration(TestServletEndpoint.class)
				.run(assertDiscoverer((discoverer) -> {
					Collection<ExposableServletEndpoint> endpoints = discoverer
							.getEndpoints();
					assertThat(endpoints).hasSize(1);
					ExposableServletEndpoint endpoint = endpoints.iterator().next();
					assertThat(endpoint.getId()).isEqualTo("testservlet");
					assertThat(endpoint.getEndpointServlet()).isNotNull();
					assertThat(endpoint).isInstanceOf(DiscoveredEndpoint.class);
				}));
	}

	@Test
	public void getEndpointsShouldDiscoverProxyServletEndpoints() {
		this.contextRunner.withUserConfiguration(TestProxyServletEndpoint.class)
				.withConfiguration(
						AutoConfigurations.of(ValidationAutoConfiguration.class))
				.run(assertDiscoverer((discoverer) -> {
					Collection<ExposableServletEndpoint> endpoints = discoverer
							.getEndpoints();
					assertThat(endpoints).hasSize(1);
					ExposableServletEndpoint endpoint = endpoints.iterator().next();
					assertThat(endpoint.getId()).isEqualTo("testservlet");
					assertThat(endpoint.getEndpointServlet()).isNotNull();
					assertThat(endpoint).isInstanceOf(DiscoveredEndpoint.class);
				}));
	}

	@Test
	public void getEndpointsShouldNotDiscoverRegularEndpoints() {
		this.contextRunner.withUserConfiguration(WithRegularEndpointConfiguration.class)
				.run(assertDiscoverer((discoverer) -> {
					Collection<ExposableServletEndpoint> endpoints = discoverer
							.getEndpoints();
					List<String> ids = endpoints.stream().map(ExposableEndpoint::getId)
							.collect(Collectors.toList());
					assertThat(ids).containsOnly("testservlet");
				}));
	}

	@Test
	public void getEndpointWhenEndpointHasOperationsShouldThrowException() {
		this.contextRunner.withUserConfiguration(TestServletEndpointWithOperation.class)
				.run(assertDiscoverer((discoverer) -> {
					this.thrown.expect(IllegalStateException.class);
					this.thrown.expectMessage(
							"ServletEndpoints must not declare operations");
					discoverer.getEndpoints();
				}));
	}

	@Test
	public void getEndpointWhenEndpointNotASupplierShouldThrowException() {
		this.contextRunner.withUserConfiguration(TestServletEndpointNotASupplier.class)
				.run(assertDiscoverer((discoverer) -> {
					this.thrown.expect(IllegalStateException.class);
					this.thrown.expectMessage("must be a supplier");
					discoverer.getEndpoints();
				}));
	}

	@Test
	public void getEndpointWhenEndpointSuppliesWrongTypeShouldThrowException() {
		this.contextRunner
				.withUserConfiguration(TestServletEndpointSupplierOfWrongType.class)
				.run(assertDiscoverer((discoverer) -> {
					this.thrown.expect(IllegalStateException.class);
					this.thrown.expectMessage("must supply an EndpointServlet");
					discoverer.getEndpoints();
				}));
	}

	@Test
	public void getEndpointWhenEndpointSuppliesNullShouldThrowException() {
		this.contextRunner.withUserConfiguration(TestServletEndpointSupplierOfNull.class)
				.run(assertDiscoverer((discoverer) -> {
					this.thrown.expect(IllegalStateException.class);
					this.thrown.expectMessage("must not supply null");
					discoverer.getEndpoints();
				}));
	}

	private ContextConsumer<AssertableApplicationContext> assertDiscoverer(
			Consumer<ServletEndpointDiscoverer> consumer) {
		return (context) -> {
			ServletEndpointDiscoverer discoverer = new ServletEndpointDiscoverer(context,
					PathMapper.useEndpointId(), Collections.emptyList());
			consumer.accept(discoverer);
		};
	}

	@Configuration
	static class EmptyConfiguration {

	}

	@Configuration
	@Import({ TestEndpoint.class, TestServletEndpoint.class })
	static class WithRegularEndpointConfiguration {

	}

	@ServletEndpoint(id = "testservlet")
	static class TestServletEndpoint implements Supplier<EndpointServlet> {

		@Override
		public EndpointServlet get() {
			return new EndpointServlet(TestServlet.class);
		}

	}

	@ServletEndpoint(id = "testservlet")
	@Validated
	static class TestProxyServletEndpoint implements Supplier<EndpointServlet> {

		@Override
		public EndpointServlet get() {
			return new EndpointServlet(TestServlet.class);
		}

	}

	@Endpoint(id = "test")
	static class TestEndpoint {

	}

	@ServletEndpoint(id = "testservlet")
	static class TestServletEndpointWithOperation implements Supplier<EndpointServlet> {

		@Override
		public EndpointServlet get() {
			return new EndpointServlet(TestServlet.class);
		}

		@ReadOperation
		public String read() {
			return "error";
		}

	}

	private static class TestServlet extends GenericServlet {

		@Override
		public void service(ServletRequest req, ServletResponse res)
				throws ServletException, IOException {
		}

	}

	@ServletEndpoint(id = "testservlet")
	static class TestServletEndpointNotASupplier {

	}

	@ServletEndpoint(id = "testservlet")
	static class TestServletEndpointSupplierOfWrongType implements Supplier<String> {

		@Override
		public String get() {
			return "error";
		}

	}

	@ServletEndpoint(id = "testservlet")
	static class TestServletEndpointSupplierOfNull implements Supplier<EndpointServlet> {

		@Override
		public EndpointServlet get() {
			return null;
		}

	}

}
