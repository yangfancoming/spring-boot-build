

package org.springframework.boot.web.servlet.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * {@link HiddenHttpMethodFilter} that also implements {@link Ordered}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public class OrderedHiddenHttpMethodFilter extends HiddenHttpMethodFilter
		implements Ordered {

	/**
	 * The default order is high to ensure the filter is applied before Spring Security.
	 */
	public static final int DEFAULT_ORDER = FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER
			- 10000;

	private int order = DEFAULT_ORDER;

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
