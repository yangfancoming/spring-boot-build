

package org.springframework.boot.logging.logback;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;

import org.springframework.core.env.Environment;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link SpringProfileAction}.
 *
 * @author Andy Wilkinson
 */
public class SpringProfileActionTests {

	private final Environment environment = mock(Environment.class);

	private final SpringProfileAction action = new SpringProfileAction(this.environment);

	private final Context context = new ContextBase();

	private final InterpretationContext interpretationContext = new InterpretationContext(
			this.context, null);

	private final Attributes attributes = mock(Attributes.class);

	@Before
	public void setUp() {
		this.action.setContext(this.context);
	}

	@Test
	public void environmentIsQueriedWithProfileFromNameAttribute()
			throws ActionException {
		given(this.attributes.getValue(Action.NAME_ATTRIBUTE)).willReturn("dev");
		this.action.begin(this.interpretationContext, null, this.attributes);
		verify(this.environment).acceptsProfiles("dev");
	}

	@Test
	public void environmentIsQueriedWithMultipleProfilesFromCommaSeparatedNameAttribute()
			throws ActionException {
		given(this.attributes.getValue(Action.NAME_ATTRIBUTE)).willReturn("dev,qa");
		this.action.begin(this.interpretationContext, null, this.attributes);
		verify(this.environment).acceptsProfiles("dev", "qa");
	}

	@Test
	public void environmentIsQueriedWithResolvedValueWhenNameAttributeUsesAPlaceholder()
			throws ActionException {
		given(this.attributes.getValue(Action.NAME_ATTRIBUTE)).willReturn("${profile}");
		this.context.putProperty("profile", "dev");
		this.action.begin(this.interpretationContext, null, this.attributes);
		verify(this.environment).acceptsProfiles("dev");
	}

	@Test
	public void environmentIsQueriedWithResolvedValuesFromCommaSeparatedNameNameAttributeWithPlaceholders()
			throws ActionException {
		given(this.attributes.getValue(Action.NAME_ATTRIBUTE))
				.willReturn("${profile1},${profile2}");
		this.context.putProperty("profile1", "dev");
		this.context.putProperty("profile2", "qa");
		this.action.begin(this.interpretationContext, null, this.attributes);
		verify(this.environment).acceptsProfiles("dev", "qa");
	}

}
