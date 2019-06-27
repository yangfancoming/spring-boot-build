

package org.springframework.boot.actuate.web.mappings.servlet;

import java.util.Collection;

import javax.servlet.FilterRegistration;

/**
 * A {@link RegistrationMappingDescription} derived from a {@link FilterRegistration}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class FilterRegistrationMappingDescription
		extends RegistrationMappingDescription<FilterRegistration> {

	/**
	 * Creates a new {@code FilterRegistrationMappingDescription} derived from the given
	 * {@code filterRegistration}.
	 * @param filterRegistration the filter registration
	 */
	public FilterRegistrationMappingDescription(FilterRegistration filterRegistration) {
		super(filterRegistration);
	}

	/**
	 * Returns the servlet name mappings for the registered filter.
	 * @return the mappings
	 */
	public Collection<String> getServletNameMappings() {
		return this.getRegistration().getServletNameMappings();
	}

	/**
	 * Returns the URL pattern mappings for the registered filter.
	 * @return the mappings
	 */
	public Collection<String> getUrlPatternMappings() {
		return this.getRegistration().getUrlPatternMappings();
	}

}
