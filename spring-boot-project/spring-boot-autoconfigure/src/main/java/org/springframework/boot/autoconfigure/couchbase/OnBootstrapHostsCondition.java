

package org.springframework.boot.autoconfigure.couchbase;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition to determine if {@code spring.couchbase.bootstrap-hosts} is specified.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 */
class OnBootstrapHostsCondition extends SpringBootCondition {

	private static final Bindable<List<String>> STRING_LIST = Bindable
			.listOf(String.class);

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		String name = "spring.couchbase.bootstrap-hosts";
		BindResult<?> property = Binder.get(context.getEnvironment()).bind(name,
				STRING_LIST);
		if (property.isBound()) {
			return ConditionOutcome.match(ConditionMessage
					.forCondition(OnBootstrapHostsCondition.class.getName())
					.found("property").items(name));
		}
		return ConditionOutcome.noMatch(
				ConditionMessage.forCondition(OnBootstrapHostsCondition.class.getName())
						.didNotFind("property").items(name));
	}

}
