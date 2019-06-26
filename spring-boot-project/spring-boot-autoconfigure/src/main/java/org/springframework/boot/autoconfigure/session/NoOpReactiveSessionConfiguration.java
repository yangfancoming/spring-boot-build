

package org.springframework.boot.autoconfigure.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveSessionRepository;

/**
 * No-op session configuration used to disable Spring Session using the environment.
 *
 * @author Tommy Ludwig
 * @author Andy Wilkinson
 */
@Configuration
@ConditionalOnMissingBean(ReactiveSessionRepository.class)
@Conditional(ReactiveSessionCondition.class)
class NoOpReactiveSessionConfiguration {

}
