

package org.springframework.boot.autoconfigure.web.servlet;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * An {@link AbstractUrlHandlerMapping} for an application's welcome page. Supports both
 * static and templated files. If both a static and templated index page are available,
 * the static page is preferred.
 *
 * @author Andy Wilkinson
 * @author Bruce Brouwer
 */
final class WelcomePageHandlerMapping extends AbstractUrlHandlerMapping {

	private static final Log logger = LogFactory.getLog(WelcomePageHandlerMapping.class);

	private static final List<MediaType> MEDIA_TYPES_ALL = Collections
			.singletonList(MediaType.ALL);

	WelcomePageHandlerMapping(TemplateAvailabilityProviders templateAvailabilityProviders,
			ApplicationContext applicationContext, Optional<Resource> welcomePage,
			String staticPathPattern) {
		if (welcomePage.isPresent() && "/**".equals(staticPathPattern)) {
			logger.info("Adding welcome page: " + welcomePage.get());
			setRootViewName("forward:index.html");
		}
		else if (welcomeTemplateExists(templateAvailabilityProviders,
				applicationContext)) {
			logger.info("Adding welcome page template: index");
			setRootViewName("index");
		}
	}

	private boolean welcomeTemplateExists(
			TemplateAvailabilityProviders templateAvailabilityProviders,
			ApplicationContext applicationContext) {
		return templateAvailabilityProviders.getProvider("index",
				applicationContext) != null;
	}

	private void setRootViewName(String viewName) {
		ParameterizableViewController controller = new ParameterizableViewController();
		controller.setViewName(viewName);
		setRootHandler(controller);
		setOrder(2);
	}

	@Override
	public Object getHandlerInternal(HttpServletRequest request) throws Exception {
		for (MediaType mediaType : getAcceptedMediaTypes(request)) {
			if (mediaType.includes(MediaType.TEXT_HTML)) {
				return super.getHandlerInternal(request);
			}
		}
		return null;
	}

	private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
		String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
		if (StringUtils.hasText(acceptHeader)) {
			return MediaType.parseMediaTypes(acceptHeader);
		}
		return MEDIA_TYPES_ALL;
	}

}
