

package org.springframework.boot.test.context.filter;

import org.junit.runner.RunWith;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Abstract test with nest {@code @Configuration} and {@code @RunWith} used by
 * {@link TestTypeExcludeFilter}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
public abstract class AbstractTestWithConfigAndRunWith {

	@Configuration
	static class Config {

	}

}
