

package org.springframework.boot.actuate.health;

import java.util.Locale;
import java.util.function.Function;

/**
 * Generate a sensible health indicator name based on its bean name.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class HealthIndicatorNameFactory implements Function<String, String> {

	@Override
	public String apply(String name) {
		int index = name.toLowerCase(Locale.ENGLISH).indexOf("healthindicator");
		if (index > 0) {
			return name.substring(0, index);
		}
		return name;
	}

}
