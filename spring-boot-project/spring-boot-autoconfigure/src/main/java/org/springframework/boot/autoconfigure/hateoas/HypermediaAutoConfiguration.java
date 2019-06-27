

package org.springframework.boot.autoconfigure.hateoas;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.LinkDiscoverers;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.plugin.core.Plugin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring HATEOAS's
 * {@link EnableHypermediaSupport}.
 *
 * @author Roy Clarkson
 * @author Oliver Gierke
 * @author Andy Wilkinson
 * @since 1.1.0
 */
@Configuration
@ConditionalOnClass({ Resource.class, RequestMapping.class, Plugin.class })
@ConditionalOnWebApplication
@AutoConfigureAfter({ WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class,
		HttpMessageConvertersAutoConfiguration.class,
		RepositoryRestMvcAutoConfiguration.class })
@EnableConfigurationProperties(HateoasProperties.class)
@Import(HypermediaHttpMessageConverterConfiguration.class)
public class HypermediaAutoConfiguration {

	@Configuration
	@ConditionalOnMissingBean(LinkDiscoverers.class)
	@ConditionalOnClass(ObjectMapper.class)
	@EnableHypermediaSupport(type = HypermediaType.HAL)
	protected static class HypermediaConfiguration {

	}

	@Configuration
	@ConditionalOnMissingBean(EntityLinks.class)
	@EnableEntityLinks
	protected static class EntityLinksConfiguration {

	}

}
