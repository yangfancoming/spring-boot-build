

package org.springframework.boot.autoconfigure.admin;

import java.util.List;

import javax.management.MalformedObjectNameException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.admin.SpringApplicationAdminMXBean;
import org.springframework.boot.admin.SpringApplicationAdminMXBeanRegistrar;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.MBeanExporter;

/**
 * Register a JMX component that allows to administer the current application. Intended for internal use only.
 * @since 1.3.0
 * @see SpringApplicationAdminMXBean
 */
@Configuration
@AutoConfigureAfter(JmxAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.application.admin", value = "enabled", havingValue = "true", matchIfMissing = false)
public class SpringApplicationAdminJmxAutoConfiguration {

	/**
	 * The property to use to customize the {@code ObjectName} of the application admin mbean.
	 */
	private static final String JMX_NAME_PROPERTY = "spring.application.admin.jmx-name";

	/**
	 * The default {@code ObjectName} of the application admin mbean.
	 */
	private static final String DEFAULT_JMX_NAME = "org.springframework.boot:type=Admin,name=SpringApplication";

	private final List<MBeanExporter> mbeanExporters;

	private final Environment environment;

	public SpringApplicationAdminJmxAutoConfiguration(ObjectProvider<List<MBeanExporter>> mbeanExporters, Environment environment) {
		this.mbeanExporters = mbeanExporters.getIfAvailable();
		this.environment = environment;
	}

	@Bean
	@ConditionalOnMissingBean
	public SpringApplicationAdminMXBeanRegistrar springApplicationAdminRegistrar() throws MalformedObjectNameException {
		String jmxName = this.environment.getProperty(JMX_NAME_PROPERTY,DEFAULT_JMX_NAME);
		if (this.mbeanExporters != null) { // Make sure to not register that MBean twice
			for (MBeanExporter mbeanExporter : this.mbeanExporters) {
				mbeanExporter.addExcludedBean(jmxName);
			}
		}
		return new SpringApplicationAdminMXBeanRegistrar(jmxName);
	}

}
