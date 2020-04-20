

package org.springframework.boot.cli.command.run;

import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringApplicationRunner}.
 */
public class SpringApplicationRunnerTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void exceptionMessageWhenSourcesContainsNoClasses() throws Exception {
		SpringApplicationRunnerConfiguration configuration = mock(SpringApplicationRunnerConfiguration.class);
		given(configuration.getClasspath()).willReturn(new String[] { "foo", "bar" });
		given(configuration.getLogLevel()).willReturn(Level.INFO);
		this.thrown.expect(RuntimeException.class);
		this.thrown.expectMessage(equalTo("No classes found in '[foo, bar]'"));
		new SpringApplicationRunner(configuration, new String[] { "foo", "bar" }).compileAndRun();

	}

}
