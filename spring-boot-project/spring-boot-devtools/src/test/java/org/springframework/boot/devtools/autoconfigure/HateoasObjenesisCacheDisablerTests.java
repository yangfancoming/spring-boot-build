

package org.springframework.boot.devtools.autoconfigure;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HateoasObjenesisCacheDisabler}.
 *
 * @author Andy Wilkinson
 */
public class HateoasObjenesisCacheDisablerTests {

	private ObjenesisStd objenesis;

	@Before
	@After
	public void resetCacheField() {
		ReflectionTestUtils.setField(HateoasObjenesisCacheDisabler.class, "cacheDisabled",
				false);
		this.objenesis = (ObjenesisStd) ReflectionTestUtils
				.getField(DummyInvocationUtils.class, "OBJENESIS");
		ReflectionTestUtils.setField(this.objenesis, "cache",
				new ConcurrentHashMap<String, ObjectInstantiator<?>>());
	}

	@Test
	public void cacheIsEnabledByDefault() {
		assertThat(this.objenesis.getInstantiatorOf(TestObject.class))
				.isSameAs(this.objenesis.getInstantiatorOf(TestObject.class));
	}

	@Test
	public void cacheIsDisabled() {
		new HateoasObjenesisCacheDisabler().afterPropertiesSet();
		assertThat(this.objenesis.getInstantiatorOf(TestObject.class))
				.isNotSameAs(this.objenesis.getInstantiatorOf(TestObject.class));
	}

	private static class TestObject {

	}

}
