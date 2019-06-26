

package org.springframework.boot.web.reactive.context;

import org.junit.Test;

import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GenericReactiveWebApplicationContext}
 *
 * @author Brian Clozel
 */
public class GenericReactiveWebApplicationContextTests {

	@Test
	public void getResourceByPath() throws Exception {
		GenericReactiveWebApplicationContext context = new GenericReactiveWebApplicationContext();
		Resource rootResource = context.getResourceByPath("/");
		assertThat(rootResource.exists()).isFalse();
		assertThat(rootResource.createRelative("application.properties").exists())
				.isFalse();
		context.close();
	}

}
