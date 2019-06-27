

package org.springframework.boot.system;

import java.io.File;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ApplicationTemp}.
 *
 * @author Phillip Webb
 */
public class ApplicationTempTests {

	@Test
	public void generatesConsistentTemp() {
		ApplicationTemp t1 = new ApplicationTemp();
		ApplicationTemp t2 = new ApplicationTemp();
		assertThat(t1.getDir()).isNotNull();
		assertThat(t1.getDir()).isEqualTo(t2.getDir());
	}

	@Test
	public void differentBasedOnUserDir() {
		String userDir = System.getProperty("user.dir");
		try {
			File t1 = new ApplicationTemp().getDir();
			System.setProperty("user.dir", "abc");
			File t2 = new ApplicationTemp().getDir();
			assertThat(t1).isNotEqualTo(t2);
		}
		finally {
			System.setProperty("user.dir", userDir);
		}
	}

	@Test
	public void getSubDir() {
		ApplicationTemp temp = new ApplicationTemp();
		assertThat(temp.getDir("abc")).isEqualTo(new File(temp.getDir(), "abc"));
	}

}
