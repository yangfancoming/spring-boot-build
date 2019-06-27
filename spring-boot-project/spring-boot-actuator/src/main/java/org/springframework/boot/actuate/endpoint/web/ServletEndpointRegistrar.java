

package org.springframework.boot.actuate.endpoint.web;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.util.Assert;

/**
 * {@link ServletContextInitializer} to register {@link ExposableServletEndpoint servlet
 * endpoints}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.0.0
 */
public class ServletEndpointRegistrar implements ServletContextInitializer {

	private static final Log logger = LogFactory.getLog(ServletEndpointRegistrar.class);

	private final String basePath;

	private final Collection<ExposableServletEndpoint> servletEndpoints;

	public ServletEndpointRegistrar(String basePath,
			Collection<ExposableServletEndpoint> servletEndpoints) {
		Assert.notNull(servletEndpoints, "ServletEndpoints must not be null");
		this.basePath = (basePath != null) ? basePath : "";
		this.servletEndpoints = servletEndpoints;
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		this.servletEndpoints
				.forEach((servletEndpoint) -> register(servletContext, servletEndpoint));
	}

	private void register(ServletContext servletContext,
			ExposableServletEndpoint endpoint) {
		String name = endpoint.getId() + "-actuator-endpoint";
		String path = this.basePath + "/" + endpoint.getRootPath();
		String urlMapping = path.endsWith("/") ? path + "*" : path + "/*";
		EndpointServlet endpointServlet = endpoint.getEndpointServlet();
		Dynamic registration = servletContext.addServlet(name,
				endpointServlet.getServlet());
		registration.addMapping(urlMapping);
		registration.setInitParameters(endpointServlet.getInitParameters());
		logger.info("Registered '" + path + "' to " + name);
	}

}
