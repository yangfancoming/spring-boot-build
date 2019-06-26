

package org.springframework.boot.admin;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;

/**
 * Register a {@link SpringApplicationAdminMXBean} implementation to the platform
 * {@link MBeanServer}.
 *
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @since 1.3.0
 */
public class SpringApplicationAdminMXBeanRegistrar implements ApplicationContextAware,
		EnvironmentAware, InitializingBean, DisposableBean {

	private static final Log logger = LogFactory.getLog(SpringApplicationAdmin.class);

	private ConfigurableApplicationContext applicationContext;

	private Environment environment = new StandardEnvironment();

	private final ObjectName objectName;

	private boolean ready = false;

	private boolean embeddedWebApplication = false;

	public SpringApplicationAdminMXBeanRegistrar(String name)
			throws MalformedObjectNameException {
		this.objectName = new ObjectName(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Assert.state(applicationContext instanceof ConfigurableApplicationContext,
				"ApplicationContext does not implement ConfigurableApplicationContext");
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@EventListener
	public void onApplicationReadyEvent(ApplicationReadyEvent event) {
		if (this.applicationContext.equals(event.getApplicationContext())) {
			this.ready = true;
		}
	}

	@EventListener
	public void onWebServerInitializedEvent(WebServerInitializedEvent event) {
		if (this.applicationContext.equals(event.getApplicationContext())) {
			this.embeddedWebApplication = true;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		server.registerMBean(new SpringApplicationAdmin(), this.objectName);
		if (logger.isDebugEnabled()) {
			logger.debug("Application Admin MBean registered with name '"
					+ this.objectName + "'");
		}
	}

	@Override
	public void destroy() throws Exception {
		ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.objectName);
	}

	private class SpringApplicationAdmin implements SpringApplicationAdminMXBean {

		@Override
		public boolean isReady() {
			return SpringApplicationAdminMXBeanRegistrar.this.ready;
		}

		@Override
		public boolean isEmbeddedWebApplication() {
			return SpringApplicationAdminMXBeanRegistrar.this.embeddedWebApplication;
		}

		@Override
		public String getProperty(String key) {
			return SpringApplicationAdminMXBeanRegistrar.this.environment
					.getProperty(key);
		}

		@Override
		public void shutdown() {
			logger.info("Application shutdown requested.");
			SpringApplicationAdminMXBeanRegistrar.this.applicationContext.close();
		}

	}

}
