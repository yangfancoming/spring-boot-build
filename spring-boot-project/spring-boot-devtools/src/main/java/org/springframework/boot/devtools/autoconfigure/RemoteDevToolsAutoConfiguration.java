

package org.springframework.boot.devtools.autoconfigure;

import java.util.Collection;

import javax.servlet.Filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties.Servlet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.devtools.remote.server.AccessManager;
import org.springframework.boot.devtools.remote.server.Dispatcher;
import org.springframework.boot.devtools.remote.server.DispatcherFilter;
import org.springframework.boot.devtools.remote.server.Handler;
import org.springframework.boot.devtools.remote.server.HandlerMapper;
import org.springframework.boot.devtools.remote.server.HttpHeaderAccessManager;
import org.springframework.boot.devtools.remote.server.HttpStatusHandler;
import org.springframework.boot.devtools.remote.server.UrlHandlerMapper;
import org.springframework.boot.devtools.restart.server.DefaultSourceFolderUrlFilter;
import org.springframework.boot.devtools.restart.server.HttpRestartServer;
import org.springframework.boot.devtools.restart.server.HttpRestartServerHandler;
import org.springframework.boot.devtools.restart.server.SourceFolderUrlFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for remote development support.
 *
 * @author Phillip Webb
 * @author Rob Winch
 * @author Andy Wilkinson
 * @since 1.3.0
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.devtools.remote", name = "secret")
@ConditionalOnClass({ Filter.class, ServerHttpRequest.class })
@EnableConfigurationProperties({ ServerProperties.class, DevToolsProperties.class })
public class RemoteDevToolsAutoConfiguration {

	private static final Log logger = LogFactory
			.getLog(RemoteDevToolsAutoConfiguration.class);

	private final DevToolsProperties properties;

	private final ServerProperties serverProperties;

	public RemoteDevToolsAutoConfiguration(DevToolsProperties properties,
			ServerProperties serverProperties) {
		this.properties = properties;
		this.serverProperties = serverProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public AccessManager remoteDevToolsAccessManager() {
		RemoteDevToolsProperties remoteProperties = this.properties.getRemote();
		return new HttpHeaderAccessManager(remoteProperties.getSecretHeaderName(),
				remoteProperties.getSecret());
	}

	@Bean
	public HandlerMapper remoteDevToolsHealthCheckHandlerMapper() {
		Handler handler = new HttpStatusHandler();
		Servlet servlet = this.serverProperties.getServlet();
		String servletContextPath = (servlet.getContextPath() != null)
				? servlet.getContextPath() : "";
		return new UrlHandlerMapper(
				servletContextPath + this.properties.getRemote().getContextPath(),
				handler);
	}

	@Bean
	@ConditionalOnMissingBean
	public DispatcherFilter remoteDevToolsDispatcherFilter(AccessManager accessManager,
			Collection<HandlerMapper> mappers) {
		Dispatcher dispatcher = new Dispatcher(accessManager, mappers);
		return new DispatcherFilter(dispatcher);
	}

	/**
	 * Configuration for remote update and restarts.
	 */
	@ConditionalOnProperty(prefix = "spring.devtools.remote.restart", name = "enabled", matchIfMissing = true)
	static class RemoteRestartConfiguration {

		@Autowired
		private DevToolsProperties properties;

		@Autowired
		private ServerProperties serverProperties;

		@Bean
		@ConditionalOnMissingBean
		public SourceFolderUrlFilter remoteRestartSourceFolderUrlFilter() {
			return new DefaultSourceFolderUrlFilter();
		}

		@Bean
		@ConditionalOnMissingBean
		public HttpRestartServer remoteRestartHttpRestartServer(
				SourceFolderUrlFilter sourceFolderUrlFilter) {
			return new HttpRestartServer(sourceFolderUrlFilter);
		}

		@Bean
		@ConditionalOnMissingBean(name = "remoteRestartHandlerMapper")
		public UrlHandlerMapper remoteRestartHandlerMapper(HttpRestartServer server) {
			Servlet servlet = this.serverProperties.getServlet();
			RemoteDevToolsProperties remote = this.properties.getRemote();
			String servletContextPath = (servlet.getContextPath() != null)
					? servlet.getContextPath() : "";
			String url = servletContextPath + remote.getContextPath() + "/restart";
			logger.warn("Listening for remote restart updates on " + url);
			Handler handler = new HttpRestartServerHandler(server);
			return new UrlHandlerMapper(url, handler);
		}

	}

}
