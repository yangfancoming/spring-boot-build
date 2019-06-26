

package org.springframework.boot.web.servlet.filter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * {@link CharacterEncodingFilter} that also implements {@link Ordered}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class OrderedCharacterEncodingFilter extends CharacterEncodingFilter
		implements Ordered {

	private int order = Ordered.HIGHEST_PRECEDENCE;

	@Override
	public int getOrder() {
		return this.order;
	}

	/**
	 * Set the order for this filter.
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
