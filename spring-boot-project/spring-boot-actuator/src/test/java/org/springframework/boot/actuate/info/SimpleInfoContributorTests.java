

package org.springframework.boot.actuate.info;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SimpleInfoContributor}.
 *
 * @author Stephane Nicoll
 */
public class SimpleInfoContributorTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void prefixIsMandatory() {
		this.thrown.expect(IllegalArgumentException.class);
		new SimpleInfoContributor(null, new Object());
	}

	@Test
	public void mapSimpleObject() {
		Object o = new Object();
		Info info = contributeFrom("test", o);
		assertThat(info.get("test")).isSameAs(o);
	}

	private static Info contributeFrom(String prefix, Object detail) {
		SimpleInfoContributor contributor = new SimpleInfoContributor(prefix, detail);
		Info.Builder builder = new Info.Builder();
		contributor.contribute(builder);
		return builder.build();
	}

}
