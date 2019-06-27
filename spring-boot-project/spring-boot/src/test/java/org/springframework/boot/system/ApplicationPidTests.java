

package org.springframework.boot.system;

import java.io.File;
import java.io.FileReader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import org.springframework.util.FileCopyUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ApplicationPid}.
 *
 * @author Phillip Webb
 */
public class ApplicationPidTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void toStringWithPid() {
		assertThat(new ApplicationPid("123").toString()).isEqualTo("123");
	}

	@Test
	public void toStringWithoutPid() {
		assertThat(new ApplicationPid(null).toString()).isEqualTo("???");
	}

	@Test
	public void throwIllegalStateWritingMissingPid() throws Exception {
		ApplicationPid pid = new ApplicationPid(null);
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("No PID available");
		pid.write(this.temporaryFolder.newFile());
	}

	@Test
	public void writePid() throws Exception {
		ApplicationPid pid = new ApplicationPid("123");
		File file = this.temporaryFolder.newFile();
		pid.write(file);
		String actual = FileCopyUtils.copyToString(new FileReader(file));
		assertThat(actual).isEqualTo("123");
	}

	@Test
	public void writeNewPid() throws Exception {
		// gh-10784
		ApplicationPid pid = new ApplicationPid("123");
		File file = this.temporaryFolder.newFile();
		file.delete();
		pid.write(file);
		String actual = FileCopyUtils.copyToString(new FileReader(file));
		assertThat(actual).isEqualTo("123");
	}

	@Test
	public void getPidFromJvm() {
		assertThat(new ApplicationPid().toString()).isNotEmpty();
	}

}
