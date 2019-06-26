

package org.springframework.boot.autoconfigure.condition;

import java.util.Map;

import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * {@link Condition} that checks for a required {@link CloudPlatform}.
 *
 * @author Madhura Bhave
 * @see ConditionalOnCloudPlatform
 */
class OnCloudPlatformCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata
				.getAnnotationAttributes(ConditionalOnCloudPlatform.class.getName());
		CloudPlatform cloudPlatform = (CloudPlatform) attributes.get("value");
		return getMatchOutcome(context.getEnvironment(), cloudPlatform);
	}

	private ConditionOutcome getMatchOutcome(Environment environment,
			CloudPlatform cloudPlatform) {
		String name = cloudPlatform.name();
		ConditionMessage.Builder message = ConditionMessage
				.forCondition(ConditionalOnCloudPlatform.class);
		if (cloudPlatform.isActive(environment)) {
			return ConditionOutcome.match(message.foundExactly(name));
		}
		return ConditionOutcome.noMatch(message.didNotFind(name).atAll());
	}

}
