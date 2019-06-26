

package org.springframework.boot.ansi;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.boot.ansi.AnsiOutput.Enabled;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AnsiOutput}.
 *
 * @author Phillip Webb
 */
public class AnsiOutputTests {

	@BeforeClass
	public static void enable() {
		AnsiOutput.setEnabled(Enabled.ALWAYS);
	}

	@AfterClass
	public static void reset() {
		AnsiOutput.setEnabled(Enabled.DETECT);
	}

	@Test
	public void encoding() {
		String encoded = AnsiOutput.toString("A", AnsiColor.RED, AnsiStyle.BOLD, "B",
				AnsiStyle.NORMAL, "D", AnsiColor.GREEN, "E", AnsiStyle.FAINT, "F");
		assertThat(encoded).isEqualTo("A[31;1mB[0mD[32mE[2mF[0;39m");
	}

}
