

package sample.aop;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.test.rule.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SampleAopApplication}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 */
public class SampleAopApplicationTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	private String profiles;

	@Before
	public void init() {
		this.profiles = System.getProperty("spring.profiles.active");
	}

	@After
	public void after() {
		if (this.profiles != null) {
			System.setProperty("spring.profiles.active", this.profiles);
		}
		else {
			System.clearProperty("spring.profiles.active");
		}
	}

	@Test
	public void testDefaultSettings() throws Exception {
		SampleAopApplication.main(new String[0]);
		String output = this.outputCapture.toString();
		assertThat(output).contains("Hello Phil");
	}

	@Test
	public void testCommandLineOverrides() throws Exception {
		SampleAopApplication.main(new String[] { "--name=Gordon" });
		String output = this.outputCapture.toString();
		assertThat(output).contains("Hello Gordon");
	}

}
