

package org.springframework.boot.actuate.endpoint.web;

import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Media types that are, by default, produced and consumed by an endpoint.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public class EndpointMediaTypes {

	private final List<String> produced;

	private final List<String> consumed;

	/**
	 * Creates a new {@link EndpointMediaTypes} with the given {@code produced} and
	 * {@code consumed} media types.
	 * @param produced the default media types that are produced by an endpoint. Must not
	 * be {@code null}.
	 * @param consumed the default media types that are consumed by an endpoint. Must not
	 */
	public EndpointMediaTypes(List<String> produced, List<String> consumed) {
		Assert.notNull(produced, "Produced must not be null");
		Assert.notNull(consumed, "Consumed must not be null");
		this.produced = Collections.unmodifiableList(produced);
		this.consumed = Collections.unmodifiableList(consumed);
	}

	/**
	 * Returns the media types produced by an endpoint.
	 * @return the produced media types
	 */
	public List<String> getProduced() {
		return this.produced;
	}

	/**
	 * Returns the media types consumed by an endpoint.
	 * @return the consumed media types
	 */
	public List<String> getConsumed() {
		return this.consumed;
	}

}
