

package org.springframework.boot.web.servlet.view;

import org.junit.Before;
import org.junit.Test;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.servlet.View;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MustacheViewResolver}.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
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
		this.resolver.setServletContext(new MockServletContext());
		this.resolver.setPrefix(this.prefix);
		this.resolver.setSuffix(".html");
	}

	@Test
	public void resolveNonExistent() throws Exception {
		assertThat(this.resolver.resolveViewName("bar", null)).isNull();
	}

	@Test
	public void resolveExisting() throws Exception {
		assertThat(this.resolver.resolveViewName("template", null)).isNotNull();
	}

	@Test
	public void setsContentType() throws Exception {
		this.resolver.setContentType("application/octet-stream");
		View view = this.resolver.resolveViewName("template", null);
		assertThat(view.getContentType()).isEqualTo("application/octet-stream");

	}

}
