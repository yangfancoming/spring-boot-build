

package org.springframework.boot.origin;

import org.springframework.util.Assert;

/**
 * Mock {@link Origin} implementation used for testing.
 *
 * @author Phillip Webb
 */
public final class MockOrigin implements Origin {

	private final String value;

	private MockOrigin(String value) {
		Assert.notNull(value, "Value must not be null");
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		return this.value.equals(((MockOrigin) obj).value);
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static Origin of(String value) {
		return (value != null) ? new MockOrigin(value) : null;
	}

}
