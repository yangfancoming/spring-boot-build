

package org.springframework.boot;

import java.lang.reflect.Method;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;

/**
 * {@link ApplicationListener} to cleanup caches once the context is loaded.
 */
class ClearCachesApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ReflectionUtils.clearCache();
		clearClassLoaderCaches(Thread.currentThread().getContextClassLoader());
	}

	private void clearClassLoaderCaches(ClassLoader classLoader) {
		if (classLoader == null) return;
		try {
			Method clearCacheMethod = classLoader.getClass().getDeclaredMethod("clearCache");
			clearCacheMethod.invoke(classLoader);
		}catch (Exception ex) {
			// Ignore
		}
		clearClassLoaderCaches(classLoader.getParent());
	}

}
