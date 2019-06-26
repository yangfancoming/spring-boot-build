

package org.springframework.boot.test.json;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.testsupport.runner.classpath.ClassPathOverrides;
import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DuplicateJsonObjectContextCustomizerFactory}.
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
@ClassPathOverrides("org.json:json:20140107")
public class DuplicateJsonObjectContextCustomizerFactoryTests {

	@Rule
	public OutputCapture output = new OutputCapture();

	@Test
	public void warningForMultipleVersions() {
		new DuplicateJsonObjectContextCustomizerFactory()
				.createContextCustomizer(null, null).customizeContext(null, null);
		assertThat(this.output.toString()).contains(
				"Found multiple occurrences of org.json.JSONObject on the class path:");
	}

}
