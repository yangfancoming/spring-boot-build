

package org.springframework.boot.autoconfigure.mustache;

import com.samskivert.mustache.Mustache.Compiler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
class MustacheServletWebConfiguration {

	private final MustacheProperties mustache;

	protected MustacheServletWebConfiguration(MustacheProperties mustache) {
		this.mustache = mustache;
	}

	@Bean
	@ConditionalOnMissingBean
	public MustacheViewResolver mustacheViewResolver(Compiler mustacheCompiler) {
		MustacheViewResolver resolver = new MustacheViewResolver(mustacheCompiler);
		this.mustache.applyToMvcViewResolver(resolver);
		resolver.setCharset(this.mustache.getCharsetName());
		resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
		return resolver;
	}

}
