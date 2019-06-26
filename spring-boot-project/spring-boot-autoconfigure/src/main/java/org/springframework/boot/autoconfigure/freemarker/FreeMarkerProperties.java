

package org.springframework.boot.autoconfigure.freemarker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for configuring FreeMarker.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
 * @since 1.1.0
 */
@ConfigurationProperties(prefix = "spring.freemarker")
public class FreeMarkerProperties extends AbstractTemplateViewResolverProperties {

	public static final String DEFAULT_TEMPLATE_LOADER_PATH = "classpath:/templates/";

	public static final String DEFAULT_PREFIX = "";

	public static final String DEFAULT_SUFFIX = ".ftl";

	/**
	 * Well-known FreeMarker keys which are passed to FreeMarker's Configuration.
	 */
	private Map<String, String> settings = new HashMap<>();

	/**
	 * Comma-separated list of template paths.
	 */
	private String[] templateLoaderPath = new String[] { DEFAULT_TEMPLATE_LOADER_PATH };

	/**
	 * Whether to prefer file system access for template loading. File system access
	 * enables hot detection of template changes.
	 */
	private boolean preferFileSystemAccess = true;

	public FreeMarkerProperties() {
		super(DEFAULT_PREFIX, DEFAULT_SUFFIX);
	}

	public Map<String, String> getSettings() {
		return this.settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public String[] getTemplateLoaderPath() {
		return this.templateLoaderPath;
	}

	public boolean isPreferFileSystemAccess() {
		return this.preferFileSystemAccess;
	}

	public void setPreferFileSystemAccess(boolean preferFileSystemAccess) {
		this.preferFileSystemAccess = preferFileSystemAccess;
	}

	public void setTemplateLoaderPath(String... templateLoaderPaths) {
		this.templateLoaderPath = templateLoaderPaths;
	}

}
