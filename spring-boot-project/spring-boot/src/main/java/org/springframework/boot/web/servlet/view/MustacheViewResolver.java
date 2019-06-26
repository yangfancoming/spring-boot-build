

package org.springframework.boot.web.servlet.view;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Compiler;

import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * Spring MVC {@link ViewResolver} for Mustache.
 *
 * @author Brian Clozel
 * @since 2.0.0
 */
public class MustacheViewResolver extends AbstractTemplateViewResolver {

	private final Mustache.Compiler compiler;

	private String charset;

	/**
	 * Create a {@code MustacheViewResolver} backed by a default instance of a
	 * {@link Compiler}.
	 */
	public MustacheViewResolver() {
		this.compiler = Mustache.compiler();
		setViewClass(requiredViewClass());
	}

	/**
	 * Create a {@code MustacheViewResolver} backed by a custom instance of a
	 * {@link Compiler}.
	 * @param compiler the Mustache compiler used to compile templates
	 */
	public MustacheViewResolver(Compiler compiler) {
		this.compiler = compiler;
		setViewClass(requiredViewClass());
	}

	@Override
	protected Class<?> requiredViewClass() {
		return MustacheView.class;
	}

	/**
	 * Set the charset.
	 * @param charset the charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		MustacheView view = (MustacheView) super.buildView(viewName);
		view.setCompiler(this.compiler);
		view.setCharset(this.charset);
		return view;
	}

}
