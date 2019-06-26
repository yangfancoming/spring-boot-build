

package org.springframework.boot.ansi;

import org.junit.After;
import org.junit.Test;

import org.springframework.boot.ansi.AnsiOutput.Enabled;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AnsiPropertySource}.
 *
 * @author Phillip Webb
 */
public class AnsiPropertySourceTests {

	private AnsiPropertySource source = new AnsiPropertySource("ansi", false);

	@After
	public void reset() {
		AnsiOutput.setEnabled(Enabled.DETECT);
	}

	@Test
	public void getAnsiStyle() {
		assertThat(this.source.getProperty("AnsiStyle.BOLD")).isEqualTo(AnsiStyle.BOLD);
	}

	@Test
	public void getAnsiColor() {
		assertThat(this.source.getProperty("AnsiColor.RED")).isEqualTo(AnsiColor.RED);
	}

	@Test
	public void getAnsiBackground() {
		assertThat(this.source.getProperty("AnsiBackground.GREEN"))
				.isEqualTo(AnsiBackground.GREEN);
	}

	@Test
	public void getAnsi() {
		assertThat(this.source.getProperty("Ansi.BOLD")).isEqualTo(AnsiStyle.BOLD);
		assertThat(this.source.getProperty("Ansi.RED")).isEqualTo(AnsiColor.RED);
		assertThat(this.source.getProperty("Ansi.BG_RED")).isEqualTo(AnsiBackground.RED);
	}

	@Test
	public void getMissing() {
		assertThat(this.source.getProperty("AnsiStyle.NOPE")).isNull();
	}

	@Test
	public void encodeEnabled() {
		AnsiOutput.setEnabled(Enabled.ALWAYS);
		AnsiPropertySource source = new AnsiPropertySource("ansi", true);
		assertThat(source.getProperty("Ansi.RED")).isEqualTo("\033[31m");
	}

	@Test
	public void encodeDisabled() {
		AnsiOutput.setEnabled(Enabled.NEVER);
		AnsiPropertySource source = new AnsiPropertySource("ansi", true);
		assertThat(source.getProperty("Ansi.RED")).isEqualTo("");
	}

}
