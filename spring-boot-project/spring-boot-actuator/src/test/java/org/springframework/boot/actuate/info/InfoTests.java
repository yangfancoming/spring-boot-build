

package org.springframework.boot.actuate.info;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Tests for {@link Info}.
 *
 * @author Stephane Nicoll
 */
public class InfoTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void infoIsImmutable() {
		Info info = new Info.Builder().withDetail("foo", "bar").build();

		this.thrown.expect(UnsupportedOperationException.class);
		info.getDetails().clear();
	}

	@Test
	public void infoTakesCopyOfMap() {
		Info.Builder builder = new Info.Builder();
		builder.withDetail("foo", "bar");
		Info build = builder.build();
		builder.withDetail("biz", "bar");
		assertThat(build.getDetails()).containsOnly(entry("foo", "bar"));
	}

}
