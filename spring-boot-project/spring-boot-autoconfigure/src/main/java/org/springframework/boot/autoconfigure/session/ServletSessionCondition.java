

package org.springframework.boot.autoconfigure.session;

import org.springframework.boot.WebApplicationType;

/**
 * General condition used with all servlet session configuration classes.
 *
 * @author Tommy Ludwig
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Andy Wilkinson
 */
class ServletSessionCondition extends AbstractSessionCondition {

	ServletSessionCondition() {
		super(WebApplicationType.SERVLET);
	}

}
