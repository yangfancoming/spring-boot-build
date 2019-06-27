

package org.springframework.boot.context.properties.bind;

import org.springframework.boot.context.properties.bind.Binder.Context;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;

/**
 * Internal strategy used by {@link Binder} to bind beans.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
interface BeanBinder {

	/**
	 * Return a bound bean instance or {@code null} if the {@link BeanBinder} does not
	 * support the specified {@link Bindable}.
	 * @param name the name being bound
	 * @param target the bindable to bind
	 * @param context the bind context
	 * @param propertyBinder property binder
	 * @param <T> the source type
	 * @return a bound instance or {@code null}
	 */
	<T> T bind(ConfigurationPropertyName name, Bindable<T> target, Context context,
			BeanPropertyBinder propertyBinder);

}
