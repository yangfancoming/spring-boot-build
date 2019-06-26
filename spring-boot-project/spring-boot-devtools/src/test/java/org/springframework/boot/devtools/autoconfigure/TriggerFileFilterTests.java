

package org.springframework.boot.devtools.autoconfigure;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TriggerFileFilter}.
 *
 * @author Phillip Webb
 */
public class TriggerFileFilterTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	@Test
	public void nameMustNotBeNull() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Name must not be null");
		new TriggerFileFilter(null);
	}

	@Test
	public void acceptNameMatch() throws Exception {
		File file = this.temp.newFile("thefile.txt");
		assertThat(new TriggerFileFilter("thefile.txt").accept(file)).isTrue();
	}

	@Test
	public void doesNotAcceptNameMismatch() throws Exception {
		File file = this.temp.newFile("notthefile.txt");
		assertThat(new TriggerFileFilter("thefile.txt").accept(file)).isFalse();
	}

	@Test
	public void testName() throws Exception {
		File file = this.temp.newFile(".triggerfile").getAbsoluteFile();
		assertThat(new TriggerFileFilter(".triggerfile").accept(file)).isTrue();
	}

}
