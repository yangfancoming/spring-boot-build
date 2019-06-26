

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTestContextHierarchyTests.ChildConfiguration;
import org.springframework.boot.test.context.SpringBootTestContextHierarchyTests.ParentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for {@link SpringBootTest} and {@link ContextHierarchy}.
 *
 * @author Andy Wilkinson
 */
@SpringBootTest
@ContextHierarchy({ @ContextConfiguration(classes = ParentConfiguration.class),
		@ContextConfiguration(classes = ChildConfiguration.class) })
@RunWith(SpringRunner.class)
public class SpringBootTestContextHierarchyTests {

	@Test
	public void contextLoads() {

	}

	@Configuration
	static class ParentConfiguration {

		@Bean
		MyBean myBean() {
			return new MyBean();
		}

	}

	@Configuration
	static class ChildConfiguration {

		ChildConfiguration(MyBean myBean) {

		}

	}

	static class MyBean {

	}

}
