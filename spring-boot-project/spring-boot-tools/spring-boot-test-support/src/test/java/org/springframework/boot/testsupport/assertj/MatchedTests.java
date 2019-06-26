

package org.springframework.boot.testsupport.assertj;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;

/**
 * Tests for {@link Matched}.
 *
 * @author Phillip Webb
 */
public class MatchedTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void byMatcherMatches() {
		assertThat("1234").is(Matched.by(startsWith("12")));
	}

	@Test
	public void byMatcherDoesNotMatch() {
		this.thrown.expect(AssertionError.class);
		this.thrown.expectMessage("a string starting with \"23\"");
		assertThat("1234").is(Matched.by(startsWith("23")));
	}

	@Test
	public void whenMatcherMatches() {
		assertThat("1234").is(Matched.when(startsWith("12")));
	}

	@Test
	public void whenMatcherDoesNotMatch() {
		this.thrown.expect(AssertionError.class);
		this.thrown.expectMessage("a string starting with \"23\"");
		assertThat("1234").is(Matched.when(startsWith("23")));
	}

}
