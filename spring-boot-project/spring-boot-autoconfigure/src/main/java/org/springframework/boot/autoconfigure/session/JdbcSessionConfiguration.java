

package org.springframework.boot.autoconfigure.session;

import java.time.Duration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.JdbcHttpSessionConfiguration;

/**
 * JDBC backed session configuration.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Vedran Pavic
 */
@Configuration
@ConditionalOnClass({ JdbcTemplate.class, JdbcOperationsSessionRepository.class })
@ConditionalOnMissingBean(SessionRepository.class)
@ConditionalOnBean(DataSource.class)
@Conditional(ServletSessionCondition.class)
@EnableConfigurationProperties(JdbcSessionProperties.class)
class JdbcSessionConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public JdbcSessionDataSourceInitializer jdbcSessionDataSourceInitializer(
			DataSource dataSource, ResourceLoader resourceLoader,
			JdbcSessionProperties properties) {
		return new JdbcSessionDataSourceInitializer(dataSource, resourceLoader,
				properties);
	}

	@Configuration
	public static class SpringBootJdbcHttpSessionConfiguration
			extends JdbcHttpSessionConfiguration {

		@Autowired
		public void customize(SessionProperties sessionProperties,
				JdbcSessionProperties jdbcSessionProperties) {
			Duration timeout = sessionProperties.getTimeout();
			if (timeout != null) {
				setMaxInactiveIntervalInSeconds((int) timeout.getSeconds());
			}
			setTableName(jdbcSessionProperties.getTableName());
			setCleanupCron(jdbcSessionProperties.getCleanupCron());
		}

	}

}
