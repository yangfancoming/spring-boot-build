

package org.springframework.boot.autoconfigure.info;

import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for various project information.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @since 1.4.0
 */
@Configuration
@EnableConfigurationProperties(ProjectInfoProperties.class)
public class ProjectInfoAutoConfiguration {

	private final ProjectInfoProperties properties;

	public ProjectInfoAutoConfiguration(ProjectInfoProperties properties) {
		this.properties = properties;
	}

	@Conditional(GitResourceAvailableCondition.class)
	@ConditionalOnMissingBean
	@Bean
	public GitProperties gitProperties() throws Exception {
		return new GitProperties(loadFrom(this.properties.getGit().getLocation(), "git"));
	}

	@ConditionalOnResource(resources = "${spring.info.build.location:classpath:META-INF/build-info.properties}")
	@ConditionalOnMissingBean
	@Bean
	public BuildProperties buildProperties() throws Exception {
		return new BuildProperties(
				loadFrom(this.properties.getBuild().getLocation(), "build"));
	}

	protected Properties loadFrom(Resource location, String prefix) throws IOException {
		String p = prefix.endsWith(".") ? prefix : prefix + ".";
		Properties source = PropertiesLoaderUtils.loadProperties(location);
		Properties target = new Properties();
		for (String key : source.stringPropertyNames()) {
			if (key.startsWith(p)) {
				target.put(key.substring(p.length()), source.get(key));
			}
		}
		return target;
	}

	static class GitResourceAvailableCondition extends SpringBootCondition {

		private final ResourceLoader defaultResourceLoader = new DefaultResourceLoader();

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			ResourceLoader loader = context.getResourceLoader();
			loader = (loader != null) ? loader : this.defaultResourceLoader;
			Environment environment = context.getEnvironment();
			String location = environment.getProperty("spring.info.git.location");
			if (location == null) {
				location = "classpath:git.properties";
			}
			ConditionMessage.Builder message = ConditionMessage
					.forCondition("GitResource");
			if (loader.getResource(location).exists()) {
				return ConditionOutcome
						.match(message.found("git info at").items(location));
			}
			return ConditionOutcome
					.noMatch(message.didNotFind("git info at").items(location));
		}

	}

}
