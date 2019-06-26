

package org.springframework.boot.autoconfigure.cache;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

/**
 * General cache condition used with all cache configuration classes.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 1.3.0
 */
class CacheCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		String sourceClass = "";
		if (metadata instanceof ClassMetadata) {
			sourceClass = ((ClassMetadata) metadata).getClassName();
		}
		ConditionMessage.Builder message = ConditionMessage.forCondition("Cache",
				sourceClass);
		Environment environment = context.getEnvironment();
		try {
			BindResult<CacheType> specified = Binder.get(environment)
					.bind("spring.cache.type", CacheType.class);
			if (!specified.isBound()) {
				return ConditionOutcome.match(message.because("automatic cache type"));
			}
			CacheType required = CacheConfigurations
					.getType(((AnnotationMetadata) metadata).getClassName());
			if (specified.get() == required) {
				return ConditionOutcome
						.match(message.because(specified.get() + " cache type"));
			}
		}
		catch (BindException ex) {
		}
		return ConditionOutcome.noMatch(message.because("unknown cache type"));
	}

}
