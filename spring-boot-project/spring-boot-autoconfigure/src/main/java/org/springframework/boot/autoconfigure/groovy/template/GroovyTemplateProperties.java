

package org.springframework.boot.autoconfigure.groovy.template;

import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for configuring Groovy templates.
 *
 * @author Dave Syer
 * @author Marten Deinum
 * @since 1.1.0
 */
@ConfigurationProperties(prefix = "spring.groovy.template", ignoreUnknownFields = true)
public class GroovyTemplateProperties extends AbstractTemplateViewResolverProperties {

	public static final String DEFAULT_RESOURCE_LOADER_PATH = "classpath:/templates/";

	public static final String DEFAULT_PREFIX = "";

	public static final String DEFAULT_SUFFIX = ".tpl";

	public static final String DEFAULT_REQUEST_CONTEXT_ATTRIBUTE = "spring";

	/**
	 * Template path.
	 */
	private String resourceLoaderPath = DEFAULT_RESOURCE_LOADER_PATH;

	public GroovyTemplateProperties() {
		super(DEFAULT_PREFIX, DEFAULT_SUFFIX);
		setRequestContextAttribute(DEFAULT_REQUEST_CONTEXT_ATTRIBUTE);
	}

	public String getResourceLoaderPath() {
		return this.resourceLoaderPath;
	}

	public void setResourceLoaderPath(String resourceLoaderPath) {
		this.resourceLoaderPath = resourceLoaderPath;
	}

}
