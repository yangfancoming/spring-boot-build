

package org.springframework.boot.devtools.autoconfigure;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Replaces the Objenesis instance in Spring HATEOAS's {@code DummyInvocationUtils} with
 * one that does not perform any caching. The cache is problematic as it's keyed on class
 * name which leads to {@code ClassCastExceptions} as the class loader changes across
 * restarts.
 *
 * @author Andy Wilkinson
 * @since 1.3.0
 */
class HateoasObjenesisCacheDisabler implements InitializingBean {

	private static final Log logger = LogFactory
			.getLog(HateoasObjenesisCacheDisabler.class);

	private static boolean cacheDisabled;

	@Override
	public void afterPropertiesSet() {
		disableCaching();
	}

	private void disableCaching() {
		if (!cacheDisabled) {
			cacheDisabled = true;
			doDisableCaching();
		}
	}

	private void doDisableCaching() {
		try {
			Class<?> type = ClassUtils.forName(
					"org.springframework.hateoas.core.DummyInvocationUtils",
					getClass().getClassLoader());
			removeObjenesisCache(type);
		}
		catch (Exception ex) {
			// Assume that Spring HATEOAS is not on the classpath and continue
		}
	}

	private void removeObjenesisCache(Class<?> dummyInvocationUtils) {
		try {
			Field objenesisField = ReflectionUtils.findField(dummyInvocationUtils,
					"OBJENESIS");
			if (objenesisField != null) {
				ReflectionUtils.makeAccessible(objenesisField);
				Object objenesis = ReflectionUtils.getField(objenesisField, null);
				Field cacheField = ReflectionUtils.findField(objenesis.getClass(),
						"cache");
				ReflectionUtils.makeAccessible(cacheField);
				ReflectionUtils.setField(cacheField, objenesis, null);
			}
		}
		catch (Exception ex) {
			logger.warn(
					"Failed to disable Spring HATEOAS's Objenesis cache. ClassCastExceptions may occur",
					ex);
		}
	}

}
