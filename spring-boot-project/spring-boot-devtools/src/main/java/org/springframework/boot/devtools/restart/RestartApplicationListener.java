

package org.springframework.boot.devtools.restart;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * {@link ApplicationListener} to initialize the {@link Restarter}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 1.3.0
 * @see Restarter
 */
public class RestartApplicationListener
		implements ApplicationListener<ApplicationEvent>, Ordered {

	private int order = HIGHEST_PRECEDENCE;

	private static final String ENABLED_PROPERTY = "spring.devtools.restart.enabled";

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ApplicationStartingEvent) {
			onApplicationStartingEvent((ApplicationStartingEvent) event);
		}
		if (event instanceof ApplicationPreparedEvent) {
			onApplicationPreparedEvent((ApplicationPreparedEvent) event);
		}
		if (event instanceof ApplicationReadyEvent
				|| event instanceof ApplicationFailedEvent) {
			Restarter.getInstance().finish();
		}
		if (event instanceof ApplicationFailedEvent) {
			onApplicationFailedEvent((ApplicationFailedEvent) event);
		}
	}

	private void onApplicationStartingEvent(ApplicationStartingEvent event) {
		// It's too early to use the Spring environment but we should still allow
		// users to disable restart using a System property.
		String enabled = System.getProperty(ENABLED_PROPERTY);
		if (enabled == null || Boolean.parseBoolean(enabled)) {
			String[] args = event.getArgs();
			DefaultRestartInitializer initializer = new DefaultRestartInitializer();
			boolean restartOnInitialize = !AgentReloader.isActive();
			Restarter.initialize(args, false, initializer, restartOnInitialize);
		}
		else {
			Restarter.disable();
		}
	}

	private void onApplicationPreparedEvent(ApplicationPreparedEvent event) {
		Restarter.getInstance().prepare(event.getApplicationContext());
	}

	private void onApplicationFailedEvent(ApplicationFailedEvent event) {
		Restarter.getInstance().remove(event.getApplicationContext());
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	/**
	 * Set the order of the listener.
	 * @param order the order of the listener
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
