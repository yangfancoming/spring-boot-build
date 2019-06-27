

package org.springframework.boot.autoconfigure.security.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The EntityScanRegistrar can cause problems with Spring security and its eager
 * instantiation needs. This test is designed to fail if the Entities can't be scanned
 * because the registrar doesn't get a callback with the right beans (essentially because
 * their instantiation order was accelerated by Security).
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JpaUserDetailsTests.Main.class, loader = SpringBootContextLoader.class)
@DirtiesContext
public class JpaUserDetailsTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Import({ EmbeddedDataSourceConfiguration.class, DataSourceAutoConfiguration.class,
			HibernateJpaAutoConfiguration.class,
			PropertyPlaceholderAutoConfiguration.class, SecurityAutoConfiguration.class })
	public static class Main {

	}

}
