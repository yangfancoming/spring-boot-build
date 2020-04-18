

package org.springframework.boot.origin;

import java.io.File;

/**
 * Interface that uniquely represents the origin of an item. For example, an item loaded
 * from a {@link File} may have an origin made up of the file name along with line/column numbers.
 * Implementations must provide sensible {@code hashCode()}, {@code equals(...)} and
 * {@code #toString()} implementations.
 * @since 2.0.0
 * @see OriginProvider
 */
public interface Origin {

	/**
	 * Find the {@link Origin} that an object originated from. Checks if the source object
	 * is an {@link OriginProvider} and also searches exception stacks.
	 * @param source the source object or {@code null}
	 * @return an optional {@link Origin}
	 */
	static Origin from(Object source) {
		if (source instanceof Origin) {
			return (Origin) source;
		}
		Origin origin = null;
		if (source != null && source instanceof OriginProvider) {
			origin = ((OriginProvider) source).getOrigin();
		}
		if (origin == null && source != null && source instanceof Throwable) {
			return from(((Throwable) source).getCause());
		}
		return origin;
	}

}
