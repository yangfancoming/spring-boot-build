

package org.springframework.boot.actuate.autoconfigure.metrics;

import java.util.Collection;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.config.MeterFilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

/**
 * {@link BeanPostProcessor} that delegates to a lazily created
 * {@link MeterRegistryConfigurer} to post-process {@link MeterRegistry} beans.
 *
 * @author Jon Schneider
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
class MeterRegistryPostProcessor implements BeanPostProcessor {

	private final ApplicationContext context;

	private volatile MeterRegistryConfigurer configurer;

	MeterRegistryPostProcessor(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof MeterRegistry) {
			getConfigurer().configure((MeterRegistry) bean);
		}
		return bean;
	}

	@SuppressWarnings("unchecked")
	private MeterRegistryConfigurer getConfigurer() {
		if (this.configurer == null) {
			this.configurer = new MeterRegistryConfigurer(beansOfType(MeterBinder.class),
					beansOfType(MeterFilter.class),
					(Collection<MeterRegistryCustomizer<?>>) (Object) beansOfType(
							MeterRegistryCustomizer.class),
					this.context.getBean(MetricsProperties.class).isUseGlobalRegistry());
		}
		return this.configurer;
	}

	private <T> Collection<T> beansOfType(Class<T> type) {
		return this.context.getBeansOfType(type).values();
	}

}
