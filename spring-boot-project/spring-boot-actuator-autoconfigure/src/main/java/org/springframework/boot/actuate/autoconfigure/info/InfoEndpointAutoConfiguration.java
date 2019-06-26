

package org.springframework.boot.actuate.autoconfigure.info;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for the {@link InfoEndpoint}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@AutoConfigureAfter(InfoContributorAutoConfiguration.class)
public class InfoEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public InfoEndpoint infoEndpoint(
			ObjectProvider<List<InfoContributor>> infoContributors) {
		return new InfoEndpoint(infoContributors.getIfAvailable(Collections::emptyList));
	}

}
