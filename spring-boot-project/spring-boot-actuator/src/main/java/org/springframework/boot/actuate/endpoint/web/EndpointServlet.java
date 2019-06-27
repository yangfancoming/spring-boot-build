

package org.springframework.boot.actuate.endpoint.web;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Contains details of a servlet that is exposed as an actuator endpoint.
 *
 * @author Phillip Webb
 */
public final class EndpointServlet {

	private final Servlet servlet;

	private final Map<String, String> initParameters;

	public EndpointServlet(Class<? extends Servlet> servlet) {
		Assert.notNull(servlet, "Servlet must not be null");
		this.servlet = BeanUtils.instantiateClass(servlet);
		this.initParameters = Collections.emptyMap();
	}

	public EndpointServlet(Servlet servlet) {
		Assert.notNull(servlet, "Servlet must not be null");
		this.servlet = servlet;
		this.initParameters = Collections.emptyMap();
	}

	private EndpointServlet(Servlet servlet, Map<String, String> initParameters) {
		this.servlet = servlet;
		this.initParameters = Collections.unmodifiableMap(initParameters);
	}

	public EndpointServlet withInitParameter(String name, String value) {
		Assert.hasText(name, "Name must not be empty");
		return withInitParameters(Collections.singletonMap(name, value));
	}

	public EndpointServlet withInitParameters(Map<String, String> initParameters) {
		Assert.notNull(initParameters, "InitParameters must not be null");
		boolean hasEmptyName = initParameters.keySet().stream()
				.anyMatch((name) -> !StringUtils.hasText(name));
		Assert.isTrue(!hasEmptyName, "InitParameters must not contain empty names");
		Map<String, String> mergedInitParameters = new LinkedHashMap<>(
				this.initParameters);
		mergedInitParameters.putAll(initParameters);
		return new EndpointServlet(this.servlet, mergedInitParameters);
	}

	Servlet getServlet() {
		return this.servlet;
	}

	Map<String, String> getInitParameters() {
		return this.initParameters;
	}

}
