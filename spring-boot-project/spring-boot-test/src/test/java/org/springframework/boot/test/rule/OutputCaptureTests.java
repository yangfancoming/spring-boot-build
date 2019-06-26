

package org.springframework.boot.test.rule;

import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OutputCapture}.
 *
 * @author Roland Weisleder
 */
public class OutputCaptureTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void toStringShouldReturnAllCapturedOutput() {
		System.out.println("Hello World");
		assertThat(this.outputCapture.toString()).contains("Hello World");
	}

	@Test
	public void reset() {
		System.out.println("Hello");
		this.outputCapture.reset();
		System.out.println("World");
		assertThat(this.outputCapture.toString()).doesNotContain("Hello")
				.contains("World");
	}

}
