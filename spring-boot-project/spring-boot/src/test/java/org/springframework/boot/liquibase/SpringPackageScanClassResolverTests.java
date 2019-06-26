

package org.springframework.boot.liquibase;

import java.util.Set;

import liquibase.logging.Logger;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SpringPackageScanClassResolver.
 *
 * @author Phillip Webb
 */
public class SpringPackageScanClassResolverTests {

	@Test
	public void testScan() {
		SpringPackageScanClassResolver resolver = new SpringPackageScanClassResolver(
				LogFactory.getLog(getClass()));
		resolver.addClassLoader(getClass().getClassLoader());
		Set<Class<?>> implementations = resolver.findImplementations(Logger.class,
				"liquibase.logging.core");
		assertThat(implementations).isNotEmpty();
	}

}
