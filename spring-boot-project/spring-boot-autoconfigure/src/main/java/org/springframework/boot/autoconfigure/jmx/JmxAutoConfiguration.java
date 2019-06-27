

package org.springframework.boot.autoconfigure.jmx;

import javax.management.MBeanServer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.MBeanExportConfiguration.SpecificPlatform;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.util.StringUtils;

/**
 * {@link EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * {@link EnableMBeanExport} mechanism based on configuration properties.
 * <p>
 * To disable auto export of annotation beans set {@code spring.jmx.enabled: false}.
 *
 * @author Christian Dupuis
 * @author Madhura Bhave
 */
@Configuration
@ConditionalOnClass({ MBeanExporter.class })
@ConditionalOnProperty(prefix = "spring.jmx", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JmxAutoConfiguration implements EnvironmentAware, BeanFactoryAware {

	private Environment environment;

	private BeanFactory beanFactory;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Bean
	@Primary
	@ConditionalOnMissingBean(value = MBeanExporter.class, search = SearchStrategy.CURRENT)
	public AnnotationMBeanExporter mbeanExporter(ObjectNamingStrategy namingStrategy) {
		AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
		exporter.setRegistrationPolicy(RegistrationPolicy.FAIL_ON_EXISTING);
		exporter.setNamingStrategy(namingStrategy);
		String serverBean = this.environment.getProperty("spring.jmx.server",
				"mbeanServer");
		if (StringUtils.hasLength(serverBean)) {
			exporter.setServer(this.beanFactory.getBean(serverBean, MBeanServer.class));
		}
		return exporter;
	}

	@Bean
	@ConditionalOnMissingBean(value = ObjectNamingStrategy.class, search = SearchStrategy.CURRENT)
	public ParentAwareNamingStrategy objectNamingStrategy() {
		ParentAwareNamingStrategy namingStrategy = new ParentAwareNamingStrategy(
				new AnnotationJmxAttributeSource());
		String defaultDomain = this.environment.getProperty("spring.jmx.default-domain");
		if (StringUtils.hasLength(defaultDomain)) {
			namingStrategy.setDefaultDomain(defaultDomain);
		}
		return namingStrategy;
	}

	@Bean
	@ConditionalOnMissingBean
	public MBeanServer mbeanServer() {
		SpecificPlatform platform = SpecificPlatform.get();
		if (platform != null) {
			return platform.getMBeanServer();
		}
		MBeanServerFactoryBean factory = new MBeanServerFactoryBean();
		factory.setLocateExistingServerIfPossible(true);
		factory.afterPropertiesSet();
		return factory.getObject();
	}

}
