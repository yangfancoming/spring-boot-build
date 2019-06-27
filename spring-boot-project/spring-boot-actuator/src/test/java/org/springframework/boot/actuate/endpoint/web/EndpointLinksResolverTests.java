

package org.springframework.boot.actuate.endpoint.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Condition;
import org.junit.Test;

import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.boot.actuate.endpoint.web.annotation.ExposableControllerEndpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link EndpointLinksResolver}.
 *
 * @author Andy Wilkinson
 */
public class EndpointLinksResolverTests {

	@Test
	public void linkResolutionWithTrailingSlashStripsSlashOnSelfLink() {
		Map<String, Link> links = new EndpointLinksResolver(Collections.emptyList())
				.resolveLinks("https://api.example.com/actuator/");
		assertThat(links).hasSize(1);
		assertThat(links).hasEntrySatisfying("self",
				linkWithHref("https://api.example.com/actuator"));
	}

	@Test
	public void linkResolutionWithoutTrailingSlash() {
		Map<String, Link> links = new EndpointLinksResolver(Collections.emptyList())
				.resolveLinks("https://api.example.com/actuator");
		assertThat(links).hasSize(1);
		assertThat(links).hasEntrySatisfying("self",
				linkWithHref("https://api.example.com/actuator"));
	}

	@Test
	public void resolvedLinksContainsALinkForEachWebEndpointOperation() {
		List<WebOperation> operations = new ArrayList<>();
		operations.add(operationWithPath("/alpha", "alpha"));
		operations.add(operationWithPath("/alpha/{name}", "alpha-name"));
		ExposableWebEndpoint endpoint = mock(ExposableWebEndpoint.class);
		given(endpoint.getId()).willReturn("alpha");
		given(endpoint.isEnableByDefault()).willReturn(true);
		given(endpoint.getOperations()).willReturn(operations);
		String requestUrl = "https://api.example.com/actuator";
		Map<String, Link> links = new EndpointLinksResolver(
				Collections.singletonList(endpoint)).resolveLinks(requestUrl);
		assertThat(links).hasSize(3);
		assertThat(links).hasEntrySatisfying("self",
				linkWithHref("https://api.example.com/actuator"));
		assertThat(links).hasEntrySatisfying("alpha",
				linkWithHref("https://api.example.com/actuator/alpha"));
		assertThat(links).hasEntrySatisfying("alpha-name",
				linkWithHref("https://api.example.com/actuator/alpha/{name}"));
	}

	@Test
	public void resolvedLinksContainsALinkForServletEndpoint() {
		ExposableServletEndpoint servletEndpoint = mock(ExposableServletEndpoint.class);
		given(servletEndpoint.getId()).willReturn("alpha");
		given(servletEndpoint.isEnableByDefault()).willReturn(true);
		given(servletEndpoint.getRootPath()).willReturn("alpha");
		String requestUrl = "https://api.example.com/actuator";
		Map<String, Link> links = new EndpointLinksResolver(
				Collections.singletonList(servletEndpoint)).resolveLinks(requestUrl);
		assertThat(links).hasSize(2);
		assertThat(links).hasEntrySatisfying("self",
				linkWithHref("https://api.example.com/actuator"));
		assertThat(links).hasEntrySatisfying("alpha",
				linkWithHref("https://api.example.com/actuator/alpha"));
	}

	@Test
	public void resolvedLinksContainsALinkForControllerEndpoint() {
		ExposableControllerEndpoint controllerEndpoint = mock(
				ExposableControllerEndpoint.class);
		given(controllerEndpoint.getId()).willReturn("alpha");
		given(controllerEndpoint.isEnableByDefault()).willReturn(true);
		given(controllerEndpoint.getRootPath()).willReturn("alpha");
		String requestUrl = "https://api.example.com/actuator";
		Map<String, Link> links = new EndpointLinksResolver(
				Collections.singletonList(controllerEndpoint)).resolveLinks(requestUrl);
		assertThat(links).hasSize(2);
		assertThat(links).hasEntrySatisfying("self",
				linkWithHref("https://api.example.com/actuator"));
		assertThat(links).hasEntrySatisfying("alpha",
				linkWithHref("https://api.example.com/actuator/alpha"));
	}

	private WebOperation operationWithPath(String path, String id) {
		WebOperationRequestPredicate predicate = new WebOperationRequestPredicate(path,
				WebEndpointHttpMethod.GET, Collections.emptyList(),
				Collections.emptyList());
		WebOperation operation = mock(WebOperation.class);
		given(operation.getId()).willReturn(id);
		given(operation.getType()).willReturn(OperationType.READ);
		given(operation.getRequestPredicate()).willReturn(predicate);
		return operation;
	}

	private Condition<Link> linkWithHref(String href) {
		return new Condition<>((link) -> href.equals(link.getHref()),
				"Link with href '%s'", href);
	}

}
