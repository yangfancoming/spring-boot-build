

package org.springframework.boot;

import java.io.IOException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ExitCodeGenerators}.
 *
 * @author Phillip Webb
 */
public class ExitCodeGeneratorsTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void addAllWhenGeneratorsIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Generators must not be null");
		List<ExitCodeGenerator> generators = null;
		new ExitCodeGenerators().addAll(generators);
	}

	@Test
	public void addWhenGeneratorIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Generator must not be null");
		new ExitCodeGenerators().add(null);
	}

	@Test
	public void getExitCodeWhenNoGeneratorsShouldReturnZero() {
		assertThat(new ExitCodeGenerators().getExitCode()).isEqualTo(0);
	}

	@Test
	public void getExitCodeWhenGeneratorThrowsShouldReturnOne() {
		ExitCodeGenerator generator = mock(ExitCodeGenerator.class);
		given(generator.getExitCode()).willThrow(new IllegalStateException());
		ExitCodeGenerators generators = new ExitCodeGenerators();
		generators.add(generator);
		assertThat(generators.getExitCode()).isEqualTo(1);
	}

	@Test
	public void getExitCodeWhenAllNegativeShouldReturnLowestValue() {
		ExitCodeGenerators generators = new ExitCodeGenerators();
		generators.add(mockGenerator(-1));
		generators.add(mockGenerator(-3));
		generators.add(mockGenerator(-2));
		assertThat(generators.getExitCode()).isEqualTo(-3);
	}

	@Test
	public void getExitCodeWhenAllPositiveShouldReturnHighestValue() {
		ExitCodeGenerators generators = new ExitCodeGenerators();
		generators.add(mockGenerator(1));
		generators.add(mockGenerator(3));
		generators.add(mockGenerator(2));
		assertThat(generators.getExitCode()).isEqualTo(3);
	}

	@Test
	public void getExitCodeWhenUsingExitCodeExceptionMapperShouldCallMapper() {
		ExitCodeGenerators generators = new ExitCodeGenerators();
		Exception e = new IOException();
		generators.add(e, mockMapper(IllegalStateException.class, 1));
		generators.add(e, mockMapper(IOException.class, 2));
		generators.add(e, mockMapper(UnsupportedOperationException.class, 3));
		assertThat(generators.getExitCode()).isEqualTo(2);
	}

	private ExitCodeGenerator mockGenerator(int exitCode) {
		ExitCodeGenerator generator = mock(ExitCodeGenerator.class);
		given(generator.getExitCode()).willReturn(exitCode);
		return generator;
	}

	private ExitCodeExceptionMapper mockMapper(Class<?> exceptionType, int exitCode) {
		return (exception) -> {
			if (exceptionType.isInstance(exception)) {
				return exitCode;
			}
			return 0;
		};
	}

}
