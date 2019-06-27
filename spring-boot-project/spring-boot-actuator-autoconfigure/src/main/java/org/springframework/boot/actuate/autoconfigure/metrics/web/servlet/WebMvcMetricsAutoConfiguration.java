

package org.springframework.boot.actuate.autoconfigure.metrics.web.servlet;

import javax.servlet.DispatcherType;

import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web.Server;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.metrics.web.servlet.DefaultWebMvcTagsProvider;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for instrumentation of Spring Web
 * MVC servlet-based request mappings.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
@Configuration
@AutoConfigureAfter({ MetricsAutoConfiguration.class,
		SimpleMetricsExportAutoConfiguration.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(DispatcherServlet.class)
@ConditionalOnBean(MeterRegistry.class)
@EnableConfigurationProperties(MetricsProperties.class)
public class WebMvcMetricsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(WebMvcTagsProvider.class)
	public DefaultWebMvcTagsProvider webMvcTagsProvider() {
		return new DefaultWebMvcTagsProvider();
	}

	@Bean
	public FilterRegistrationBean<WebMvcMetricsFilter> webMvcMetricsFilter(
			MeterRegistry registry, MetricsProperties properties,
			WebMvcTagsProvider tagsProvider, WebApplicationContext context) {
		Server serverProperties = properties.getWeb().getServer();
		WebMvcMetricsFilter filter = new WebMvcMetricsFilter(context, registry,
				tagsProvider, serverProperties.getRequestsMetricName(),
				serverProperties.isAutoTimeRequests());
		FilterRegistrationBean<WebMvcMetricsFilter> registration = new FilterRegistrationBean<>(
				filter);
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
		return registration;
	}

}
