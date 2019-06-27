

package org.springframework.boot.autoconfigure.mustache;

import com.samskivert.mustache.Mustache.Compiler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.web.reactive.result.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnWebApplication(type = Type.REACTIVE)
class MustacheReactiveWebConfiguration {

	private final MustacheProperties mustache;

	protected MustacheReactiveWebConfiguration(MustacheProperties mustache) {
		this.mustache = mustache;
	}

	@Bean
	@ConditionalOnMissingBean
	public MustacheViewResolver mustacheViewResolver(Compiler mustacheCompiler) {
		MustacheViewResolver resolver = new MustacheViewResolver(mustacheCompiler);
		resolver.setPrefix(this.mustache.getPrefix());
		resolver.setSuffix(this.mustache.getSuffix());
		resolver.setViewNames(this.mustache.getViewNames());
		resolver.setRequestContextAttribute(this.mustache.getRequestContextAttribute());
		resolver.setCharset(this.mustache.getCharsetName());
		resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
		return resolver;
	}

}
