

package org.springframework.boot.actuate.autoconfigure.integrationtest;

import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.actuate.autoconfigure.beans.BeansEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.reactive.WebFluxEndpointManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.reactive.ReactiveManagementContextAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the WebFlux actuator endpoints' CORS support
 *
 * @author Brian Clozel
 * @see WebFluxEndpointManagementContextConfiguration
 */
public class WebFluxEndpointCorsIntegrationTests {

	private AnnotationConfigReactiveWebApplicationContext context;

	@Before
	public void createContext() {
		this.context = new AnnotationConfigReactiveWebApplicationContext();
		this.context.register(JacksonAutoConfiguration.class,
				CodecsAutoConfiguration.class, WebFluxAutoConfiguration.class,
				HttpHandlerAutoConfiguration.class, EndpointAutoConfiguration.class,
				WebEndpointAutoConfiguration.class,
				ManagementContextAutoConfiguration.class,
				ReactiveManagementContextAutoConfiguration.class,
				BeansEndpointAutoConfiguration.class);
		TestPropertyValues.of("management.endpoints.web.exposure.include:*")
				.applyTo(this.context);
	}

	@Test
	public void corsIsDisabledByDefault() {
		createWebTestClient().options().uri("/actuator/beans")
				.header("Origin", "spring.example.org")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET").exchange()
				.expectStatus().isForbidden().expectHeader()
				.doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
	}

	@Test
	public void settingAllowedOriginsEnablesCors() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org")
				.applyTo(this.context);
		createWebTestClient().options().uri("/actuator/beans")
				.header("Origin", "test.example.org")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET").exchange()
				.expectStatus().isForbidden();
		performAcceptedCorsRequest("/actuator/beans");
	}

	@Test
	public void maxAgeDefaultsTo30Minutes() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org")
				.applyTo(this.context);
		performAcceptedCorsRequest("/actuator/beans").expectHeader()
				.valueEquals(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1800");
	}

	@Test
	public void maxAgeCanBeConfigured() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org",
						"management.endpoints.web.cors.max-age: 2400")
				.applyTo(this.context);
		performAcceptedCorsRequest("/actuator/beans").expectHeader()
				.valueEquals(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "2400");
	}

	@Test
	public void requestsWithDisallowedHeadersAreRejected() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org")
				.applyTo(this.context);
		createWebTestClient().options().uri("/actuator/beans")
				.header("Origin", "spring.example.org")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Alpha").exchange()
				.expectStatus().isForbidden();
	}

	@Test
	public void allowedHeadersCanBeConfigured() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org",
						"management.endpoints.web.cors.allowed-headers:Alpha,Bravo")
				.applyTo(this.context);
		createWebTestClient().options().uri("/actuator/beans")
				.header("Origin", "spring.example.org")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Alpha").exchange()
				.expectStatus().isOk().expectHeader()
				.valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Alpha");
	}

	@Test
	public void requestsWithDisallowedMethodsAreRejected() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org")
				.applyTo(this.context);
		createWebTestClient().options().uri("/actuator/beans")
				.header("Origin", "spring.example.org")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PATCH").exchange()
				.expectStatus().isForbidden();
	}

	@Test
	public void allowedMethodsCanBeConfigured() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org",
						"management.endpoints.web.cors.allowed-methods:GET,HEAD")
				.applyTo(this.context);
		createWebTestClient().options().uri("/actuator/beans")
				.header("Origin", "spring.example.org")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "HEAD").exchange()
				.expectStatus().isOk().expectHeader()
				.valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,HEAD");
	}

	@Test
	public void credentialsCanBeAllowed() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org",
						"management.endpoints.web.cors.allow-credentials:true")
				.applyTo(this.context);
		performAcceptedCorsRequest("/actuator/beans").expectHeader()
				.valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
	}

	@Test
	public void credentialsCanBeDisabled() {
		TestPropertyValues
				.of("management.endpoints.web.cors.allowed-origins:spring.example.org",
						"management.endpoints.web.cors.allow-credentials:false")
				.applyTo(this.context);
		performAcceptedCorsRequest("/actuator/beans").expectHeader()
				.doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
	}

	private WebTestClient createWebTestClient() {
		this.context.refresh();
		return WebTestClient.bindToApplicationContext(this.context).configureClient()
				.baseUrl("https://spring.example.org").build();
	}

	private WebTestClient.ResponseSpec performAcceptedCorsRequest(String url) {
		return createWebTestClient().options().uri(url)
				.header(HttpHeaders.ORIGIN, "spring.example.org")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET").exchange()
				.expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
						"spring.example.org")
				.expectStatus().isOk();
	}

}
