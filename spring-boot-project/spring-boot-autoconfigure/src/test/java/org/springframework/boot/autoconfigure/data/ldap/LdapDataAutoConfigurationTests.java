

package org.springframework.boot.autoconfigure.data.ldap;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ldap.core.LdapTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LdapDataAutoConfiguration}
 *
 * @author Eddú Meléndez
 */
public class LdapDataAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void templateExists() {
		this.context = new AnnotationConfigApplicationContext();
		TestPropertyValues.of("spring.ldap.urls:ldap://localhost:389")
				.applyTo(this.context);
		this.context.register(PropertyPlaceholderAutoConfiguration.class,
				LdapAutoConfiguration.class, LdapDataAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBeanNamesForType(LdapTemplate.class)).hasSize(1);
	}

}
