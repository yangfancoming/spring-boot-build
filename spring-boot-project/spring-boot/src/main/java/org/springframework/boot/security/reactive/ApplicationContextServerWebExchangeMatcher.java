

package org.springframework.boot.security.reactive;

import java.util.function.Supplier;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

/**
 * {@link ApplicationContext} backed {@link ServerWebExchangeMatcher}. Can work directly
 * with the {@link ApplicationContext}, obtain an existing bean or
 * {@link AutowireCapableBeanFactory#createBean(Class, int, boolean) create a new bean}
 * that is autowired in the usual way.
 *
 * @param <C> the type of the context that the match method actually needs to use. Can be
 * an {@link ApplicationContext} or a class of an {@link ApplicationContext#getBean(Class)
 * existing bean}.
 * @author Madhura Bhave
 * @since 2.0.0
 */
public abstract class ApplicationContextServerWebExchangeMatcher<C>
		implements ServerWebExchangeMatcher {

	private final Class<? extends C> contextClass;

	private volatile Supplier<C> context;

	private final Object contextLock = new Object();

	public ApplicationContextServerWebExchangeMatcher(Class<? extends C> contextClass) {
		Assert.notNull(contextClass, "Context class must not be null");
		this.contextClass = contextClass;
	}

	@Override
	public final Mono<MatchResult> matches(ServerWebExchange exchange) {
		return matches(exchange, getContext(exchange));
	}

	/**
	 * Decides whether the rule implemented by the strategy matches the supplied exchange.
	 * @param exchange the source exchange
	 * @param context a supplier for the initialized context (may throw an exception)
	 * @return if the exchange matches
	 */
	protected abstract Mono<MatchResult> matches(ServerWebExchange exchange,
			Supplier<C> context);

	protected Supplier<C> getContext(ServerWebExchange exchange) {
		if (this.context == null) {
			synchronized (this.contextLock) {
				if (this.context == null) {
					this.context = createContext(exchange);
					initialized(this.context);
				}
			}
		}
		return this.context;
	}

	/**
	 * Called once the context has been initialized.
	 * @param context a supplier for the initialized context (may throw an exception)
	 */
	protected void initialized(Supplier<C> context) {
	}

	@SuppressWarnings("unchecked")
	private Supplier<C> createContext(ServerWebExchange exchange) {
		ApplicationContext context = exchange.getApplicationContext();
		Assert.state(context != null,
				"No ApplicationContext found on ServerWebExchange.");
		if (this.contextClass.isInstance(context)) {
			return () -> (C) context;
		}
		return () -> context.getBean(this.contextClass);
	}

}
