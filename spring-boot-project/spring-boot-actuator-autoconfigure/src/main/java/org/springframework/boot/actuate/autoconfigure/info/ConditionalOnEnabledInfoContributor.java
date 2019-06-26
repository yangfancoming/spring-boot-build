

package org.springframework.boot.actuate.autoconfigure.info;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * {@link Conditional} that checks whether or not a default info contributor is enabled.
 * Matches if the value of the {@code management.info.<name>.enabled} property is
 * {@code true}. Otherwise, matches if the value of the
 * {@code management.info.defaults.enabled} property is {@code true} or if it is not
 * configured.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Conditional(OnEnabledInfoContributorCondition.class)
public @interface ConditionalOnEnabledInfoContributor {

	/**
	 * The name of the info contributor.
	 * @return the name of the info contributor
	 */
	String value();

}
