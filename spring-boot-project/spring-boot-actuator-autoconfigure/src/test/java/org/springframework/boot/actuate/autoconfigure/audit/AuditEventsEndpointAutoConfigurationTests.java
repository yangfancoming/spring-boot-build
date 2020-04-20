

package org.springframework.boot.actuate.autoconfigure.audit;

import org.junit.Test;

import org.springframework.boot.actuate.audit.AuditEventsEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AuditEventsEndpointAutoConfiguration}.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @author Vedran Pavic
 */
public class AuditEventsEndpointAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(AuditAutoConfiguration.class,
					AuditEventsEndpointAutoConfiguration.class));

	@Test
	public void runShouldHaveEndpointBean() {
		this.contextRunner.run((context) -> assertThat(context)
				.hasSingleBean(AuditEventsEndpoint.class));
	}

	@Test
	public void runWhenEnabledPropertyIsFalseShouldNotHaveEndpoint() {
		this.contextRunner
				.withPropertyValues("management.endpoint.auditevents.enabled:false")
				.run((context) -> assertThat(context)
						.doesNotHaveBean(AuditEventsEndpoint.class));
	}

}
