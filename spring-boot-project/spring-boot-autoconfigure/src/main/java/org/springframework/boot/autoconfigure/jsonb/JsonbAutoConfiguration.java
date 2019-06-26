

package org.springframework.boot.autoconfigure.jsonb;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for JSON-B.
 *
 * @author Eddú Meléndez
 * @since 2.0.0
 */
@Configuration
@ConditionalOnClass(Jsonb.class)
public class JsonbAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Jsonb jsonb() {
		return JsonbBuilder.create();
	}

}
