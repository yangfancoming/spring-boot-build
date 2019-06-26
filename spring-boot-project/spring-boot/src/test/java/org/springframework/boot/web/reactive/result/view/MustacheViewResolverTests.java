

package org.springframework.boot.web.reactive.result.view;

import org.junit.Before;
import org.junit.Test;

import org.springframework.context.support.GenericApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MustacheViewResolver}.
 *
 * @author Brian Clozel
 */
public class MustacheViewResolverTests {

	private final String prefix = "classpath:/"
			+ getClass().getPackage().getName().replace(".", "/") + "/";

	private MustacheViewResolver resolver = new MustacheViewResolver();

	@Before
	public void init() {
		GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.refresh();
		this.resolver.setApplicationContext(applicationContext);
		this.resolver.setPrefix(this.prefix);
		this.resolver.setSuffix(".html");
	}

	@Test
	public void resolveNonExistent() {
		assertThat(this.resolver.resolveViewName("bar", null).block()).isNull();
	}

	@Test
	public void resolveExisting() {
		assertThat(this.resolver.resolveViewName("template", null).block()).isNotNull();
	}

}
