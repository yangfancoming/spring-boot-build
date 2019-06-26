

package org.springframework.boot.actuate.autoconfigure.web.servlet;

import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * {@link Controller} for handling "/error" path when the management servlet is in a child
 * context. The regular {@link ErrorController} should be available there but because of
 * the way the handler mappings are set up it will not be detected.
 *
 * @author Dave Syer
 * @since 2.0.0
 */
@Controller
public class ManagementErrorEndpoint {

	private final ErrorAttributes errorAttributes;

	public ManagementErrorEndpoint(ErrorAttributes errorAttributes) {
		Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping("${server.error.path:${error.path:/error}}")
	@ResponseBody
	public Map<String, Object> invoke(ServletWebRequest request) {
		return this.errorAttributes.getErrorAttributes(request, false);
	}

}
