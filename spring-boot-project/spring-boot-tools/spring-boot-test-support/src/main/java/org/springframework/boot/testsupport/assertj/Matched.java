

package org.springframework.boot.testsupport.assertj;

import org.assertj.core.api.Condition;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import org.springframework.util.Assert;

/**
 * Adapter class allowing a Hamcrest {@link Matcher} to be used as an AssertJ
 * {@link Condition}.
 *
 * @param <T> the type of object that the condition accepts
 * @author Phillip Webb
 */
public final class Matched<T> extends Condition<T> {

	private final Matcher<? extends T> matcher;

	private Matched(Matcher<? extends T> matcher) {
		Assert.notNull(matcher, "Matcher must not be null");
		this.matcher = matcher;
	}

	@Override
	public boolean matches(T value) {
		if (this.matcher.matches(value)) {
			return true;
		}
		StringDescription description = new StringDescription();
		this.matcher.describeTo(description);
		describedAs(description.toString());
		return false;
	}

	public static <T> Condition<T> when(Matcher<? extends T> matcher) {
		return by(matcher);
	}

	public static <T> Condition<T> by(Matcher<? extends T> matcher) {
		return new Matched<>(matcher);
	}

}
