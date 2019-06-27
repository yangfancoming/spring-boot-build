

package org.springframework.boot.context.logging;

import java.net.URLClassLoader;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.ResolvableType;

/**
 * A {@link SmartApplicationListener} that reacts to
 * {@link ApplicationEnvironmentPreparedEvent environment prepared events} and to
 * {@link ApplicationFailedEvent failed events} by logging the classpath of the thread
 * context class loader (TCCL) at {@code DEBUG} level.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public final class ClasspathLoggingApplicationListener
		implements GenericApplicationListener {

	private static final int ORDER = LoggingApplicationListener.DEFAULT_ORDER + 1;

	private static final Log logger = LogFactory
			.getLog(ClasspathLoggingApplicationListener.class);

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (logger.isDebugEnabled()) {
			if (event instanceof ApplicationEnvironmentPreparedEvent) {
				logger.debug("Application started with classpath: " + getClasspath());
			}
			else if (event instanceof ApplicationFailedEvent) {
				logger.debug(
						"Application failed to start with classpath: " + getClasspath());
			}
		}
	}

	@Override
	public int getOrder() {
		return ORDER;
	}

	@Override
	public boolean supportsEventType(ResolvableType resolvableType) {
		Class<?> type = resolvableType.getRawClass();
		if (type == null) {
			return false;
		}
		return ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(type)
				|| ApplicationFailedEvent.class.isAssignableFrom(type);
	}

	@Override
	public boolean supportsSourceType(Class<?> sourceType) {
		return true;
	}

	private String getClasspath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader instanceof URLClassLoader) {
			return Arrays.toString(((URLClassLoader) classLoader).getURLs());
		}
		return "unknown";
	}

}
