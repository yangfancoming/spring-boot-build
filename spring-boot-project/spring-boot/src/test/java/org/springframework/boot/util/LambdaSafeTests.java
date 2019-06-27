

package org.springframework.boot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.util.LambdaSafe.Filter;
import org.springframework.boot.util.LambdaSafe.InvocationResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for {@link LambdaSafe}.
 *
 * @author Phillip Webb
 */
public class LambdaSafeTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void callbackWhenCallbackTypeIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("CallbackType must not be null");
		LambdaSafe.callback(null, new Object(), null);
	}

	@Test
	public void callbackWhenCallbackInstanceIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("CallbackInstance must not be null");
		LambdaSafe.callback(Object.class, null, null);
	}

	@Test
	public void callbackInvokeWhenNoGenericShouldInvokeCallback() {
		NonGenericCallback callbackInstance = mock(NonGenericCallback.class);
		String argument = "foo";
		LambdaSafe.callback(NonGenericCallback.class, callbackInstance, argument)
				.invoke((c) -> c.handle(argument));
		verify(callbackInstance).handle(argument);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeWhenHasGenericShouldInvokeCallback() {
		StringCallback callbackInstance = mock(StringCallback.class);
		String argument = "foo";
		LambdaSafe.callback(GenericCallback.class, callbackInstance, argument)
				.invoke((c) -> c.handle(argument));
		verify(callbackInstance).handle(argument);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeWhenHasResolvableGenericMatchShouldInvokeCallback() {
		StringBuilderCallback callbackInstance = mock(StringBuilderCallback.class);
		StringBuilder argument = new StringBuilder("foo");
		LambdaSafe.callback(GenericCallback.class, callbackInstance, argument)
				.invoke((c) -> c.handle(argument));
		verify(callbackInstance).handle(argument);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeWhenHasResolvableGenericNonMatchShouldNotInvokeCallback() {
		GenericCallback<?> callbackInstance = mock(StringBuilderCallback.class);
		String argument = "foo";
		LambdaSafe.callback(GenericCallback.class, callbackInstance, argument)
				.invoke((c) -> c.handle(argument));
		verifyZeroInteractions(callbackInstance);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeWhenLambdaMismatchShouldSwallowException() {
		GenericCallback<StringBuilder> callbackInstance = (s) -> fail(
				"Should not get here");
		String argument = "foo";
		LambdaSafe.callback(GenericCallback.class, callbackInstance, argument)
				.invoke((c) -> c.handle(argument));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeWhenLambdaMismatchOnDifferentArgumentShouldSwallowException() {
		GenericMultiArgCallback<StringBuilder> callbackInstance = (n, s,
				b) -> fail("Should not get here");
		String argument = "foo";
		LambdaSafe.callback(GenericMultiArgCallback.class, callbackInstance, argument)
				.invoke((c) -> c.handle(1, argument, false));
	}

	@Test
	public void callbackInvokeAndWhenNoGenericShouldReturnResult() {
		NonGenericFactory callbackInstance = mock(NonGenericFactory.class);
		String argument = "foo";
		given(callbackInstance.handle("foo")).willReturn(123);
		InvocationResult<Integer> result = LambdaSafe
				.callback(NonGenericFactory.class, callbackInstance, argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result.hasResult()).isTrue();
		assertThat(result.get()).isEqualTo(123);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeAndWhenHasGenericShouldReturnResult() {
		StringFactory callbackInstance = mock(StringFactory.class);
		String argument = "foo";
		given(callbackInstance.handle("foo")).willReturn(123);
		InvocationResult<Integer> result = LambdaSafe
				.callback(GenericFactory.class, callbackInstance, argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result.hasResult()).isTrue();
		assertThat(result.get()).isEqualTo(123);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeAndWhenReturnNullShouldReturnResult() {
		StringFactory callbackInstance = mock(StringFactory.class);
		String argument = "foo";
		given(callbackInstance.handle("foo")).willReturn(null);
		InvocationResult<Integer> result = LambdaSafe
				.callback(GenericFactory.class, callbackInstance, argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result.hasResult()).isTrue();
		assertThat(result.get()).isNull();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeAndWhenHasResolvableGenericMatchShouldReturnResult() {
		StringBuilderFactory callbackInstance = mock(StringBuilderFactory.class);
		StringBuilder argument = new StringBuilder("foo");
		given(callbackInstance.handle(any(StringBuilder.class))).willReturn(123);
		InvocationResult<Integer> result = LambdaSafe
				.callback(GenericFactory.class, callbackInstance, argument)
				.invokeAnd((c) -> c.handle(argument));
		verify(callbackInstance).handle(argument);
		assertThat(result.hasResult()).isTrue();
		assertThat(result.get()).isEqualTo(123);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeAndWhenHasResolvableGenericNonMatchShouldReturnNoResult() {
		GenericFactory<?> callbackInstance = mock(StringBuilderFactory.class);
		String argument = "foo";
		InvocationResult<Integer> result = LambdaSafe
				.callback(GenericFactory.class, callbackInstance, argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result.hasResult()).isFalse();
		verifyZeroInteractions(callbackInstance);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeAndWhenLambdaMismatchShouldSwallowException() {
		GenericFactory<StringBuilder> callbackInstance = (s) -> {
			fail("Should not get here");
			return 123;
		};
		String argument = "foo";
		InvocationResult<Integer> result = LambdaSafe
				.callback(GenericFactory.class, callbackInstance, argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result.hasResult()).isFalse();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackInvokeAndWhenLambdaMismatchOnDifferentArgumentShouldSwallowException() {
		GenericMultiArgFactory<StringBuilder> callbackInstance = (n, s, b) -> {
			fail("Should not get here");
			return 123;
		};
		String argument = "foo";
		InvocationResult<Integer> result = LambdaSafe
				.callback(GenericMultiArgFactory.class, callbackInstance, argument)
				.invokeAnd((c) -> c.handle(1, argument, false));
		assertThat(result.hasResult()).isFalse();
	}

	@Test
	public void callbacksInvokeWhenNoGenericShouldInvokeCallbacks() {
		NonGenericCallback callbackInstance = mock(NonGenericCallback.class);
		String argument = "foo";
		LambdaSafe
				.callbacks(NonGenericCallback.class,
						Collections.singleton(callbackInstance), argument)
				.invoke((c) -> c.handle(argument));
		verify(callbackInstance).handle(argument);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeWhenHasGenericShouldInvokeCallback() {
		StringCallback callbackInstance = mock(StringCallback.class);
		String argument = "foo";
		LambdaSafe.callbacks(GenericCallback.class,
				Collections.singleton(callbackInstance), argument)
				.invoke((c) -> c.handle(argument));
		verify(callbackInstance).handle(argument);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeWhenHasResolvableGenericMatchShouldInvokeCallback() {
		StringBuilderCallback callbackInstance = mock(StringBuilderCallback.class);
		StringBuilder argument = new StringBuilder("foo");
		LambdaSafe.callbacks(GenericCallback.class,
				Collections.singleton(callbackInstance), argument)
				.invoke((c) -> c.handle(argument));
		verify(callbackInstance).handle(argument);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeWhenHasResolvableGenericNonMatchShouldNotInvokeCallback() {
		GenericCallback<?> callbackInstance = mock(StringBuilderCallback.class);
		String argument = "foo";
		LambdaSafe.callbacks(GenericCallback.class,
				Collections.singleton(callbackInstance), argument)
				.invoke((c) -> c.handle(null));
		verifyZeroInteractions(callbackInstance);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeWhenLambdaMismatchShouldSwallowException() {
		GenericCallback<StringBuilder> callbackInstance = (s) -> fail(
				"Should not get here");
		String argument = "foo";
		LambdaSafe.callbacks(GenericCallback.class,
				Collections.singleton(callbackInstance), argument)
				.invoke((c) -> c.handle(argument));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeWhenLambdaMismatchOnDifferentArgumentShouldSwallowException() {
		GenericMultiArgCallback<StringBuilder> callbackInstance = (n, s,
				b) -> fail("Should not get here");
		String argument = "foo";
		LambdaSafe
				.callbacks(GenericMultiArgCallback.class,
						Collections.singleton(callbackInstance), argument)
				.invoke((c) -> c.handle(1, argument, false));
	}

	@Test
	public void callbacksInvokeAndWhenNoGenericShouldReturnResult() {
		NonGenericFactory callbackInstance = mock(NonGenericFactory.class);
		String argument = "foo";
		given(callbackInstance.handle("foo")).willReturn(123);
		Stream<Integer> result = LambdaSafe
				.callbacks(NonGenericFactory.class,
						Collections.singleton(callbackInstance), argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result).containsExactly(123);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeAndWhenHasGenericShouldReturnResult() {
		StringFactory callbackInstance = mock(StringFactory.class);
		String argument = "foo";
		given(callbackInstance.handle("foo")).willReturn(123);
		Stream<Integer> result = LambdaSafe.callbacks(GenericFactory.class,
				Collections.singleton(callbackInstance), argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result).containsExactly(123);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeAndWhenReturnNullShouldReturnResult() {
		StringFactory callbackInstance = mock(StringFactory.class);
		String argument = "foo";
		given(callbackInstance.handle("foo")).willReturn(null);
		Stream<Integer> result = LambdaSafe.callbacks(GenericFactory.class,
				Collections.singleton(callbackInstance), argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result).containsExactly((Integer) null);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeAndWhenHasResolvableGenericMatchShouldReturnResult() {
		StringBuilderFactory callbackInstance = mock(StringBuilderFactory.class);
		StringBuilder argument = new StringBuilder("foo");
		given(callbackInstance.handle(any(StringBuilder.class))).willReturn(123);
		Stream<Integer> result = LambdaSafe.callbacks(GenericFactory.class,
				Collections.singleton(callbackInstance), argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result).containsExactly(123);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeAndWhenHasResolvableGenericNonMatchShouldReturnNoResult() {
		GenericFactory<?> callbackInstance = mock(StringBuilderFactory.class);
		String argument = "foo";
		Stream<Integer> result = LambdaSafe.callbacks(GenericFactory.class,
				Collections.singleton(callbackInstance), argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result).isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeAndWhenLambdaMismatchShouldSwallowException() {
		GenericFactory<StringBuilder> callbackInstance = (s) -> {
			fail("Should not get here");
			return 123;
		};
		String argument = "foo";
		Stream<Integer> result = LambdaSafe.callbacks(GenericFactory.class,
				Collections.singleton(callbackInstance), argument)
				.invokeAnd((c) -> (c).handle(argument));
		assertThat(result).isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeAndWhenLambdaMismatchOnDifferentArgumentShouldSwallowException() {
		GenericMultiArgFactory<StringBuilder> callbackInstance = (n, s, b) -> {
			fail("Should not get here");
			return 123;
		};
		String argument = "foo";
		Stream<Integer> result = LambdaSafe
				.callbacks(GenericMultiArgFactory.class,
						Collections.singleton(callbackInstance), argument)
				.invokeAnd((c) -> c.handle(1, argument, false));
		assertThat(result).isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbacksInvokeWhenMultipleShouldInvokeSuitable() {
		List<GenericFactory<?>> callbackInstances = new ArrayList<>();
		GenericFactory<String> callback1 = (s) -> 1;
		GenericFactory<CharSequence> callback2 = (s) -> 2;
		GenericFactory<StringBuilder> callback3 = (s) -> 3;
		StringFactory callback4 = mock(StringFactory.class);
		given(callback4.handle(any(String.class))).willReturn(4);
		StringBuilderFactory callback5 = mock(StringBuilderFactory.class);
		given(callback5.handle(any(StringBuilder.class))).willReturn(5);
		callbackInstances.add(callback1);
		callbackInstances.add(callback2);
		callbackInstances.add(callback3);
		callbackInstances.add(callback4);
		callbackInstances.add(callback5);
		String argument = "foo";
		Stream<Integer> result = LambdaSafe
				.callbacks(GenericFactory.class, callbackInstances, argument)
				.invokeAnd((c) -> c.handle(argument));
		assertThat(result).containsExactly(1, 2, 4);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackWithFilterShouldUseFilter() {
		GenericCallback<?> callbackInstance = mock(StringBuilderCallback.class);
		String argument = "foo";
		LambdaSafe.callback(GenericCallback.class, callbackInstance, argument)
				.withFilter(Filter.allowAll()).invoke((c) -> c.handle(null));
		verify(callbackInstance).handle(null);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void callbackWithLoggerShouldUseLogger() {
		Log logger = mock(Log.class);
		given(logger.isDebugEnabled()).willReturn(true);
		GenericCallback<StringBuilder> callbackInstance = (s) -> fail(
				"Should not get here");
		String argument = "foo";
		LambdaSafe.callback(GenericCallback.class, callbackInstance, argument)
				.withLogger(logger).invoke((c) -> c.handle(argument));
		verify(logger).debug(contains("Non-matching CharSequence type for callback "
				+ "LambdaSafeTests.GenericCallback"), any(Throwable.class));
	}

	interface NonGenericCallback {

		void handle(String argument);

	}

	interface GenericCallback<T extends CharSequence> {

		void handle(T argument);

	}

	interface StringCallback extends GenericCallback<String> {

	}

	interface StringBuilderCallback extends GenericCallback<StringBuilder> {

	}

	interface GenericMultiArgCallback<T extends CharSequence> {

		void handle(Integer number, T argument, Boolean bool);

	}

	interface NonGenericFactory {

		Integer handle(String argument);

	}

	interface GenericFactory<T extends CharSequence> {

		Integer handle(T argument);

	}

	interface StringFactory extends GenericFactory<String> {

	}

	interface StringBuilderFactory extends GenericFactory<StringBuilder> {

	}

	interface GenericMultiArgFactory<T extends CharSequence> {

		Integer handle(Integer number, T argument, Boolean bool);

	}

}
