

package org.springframework.boot.cli.command.shell;

import jline.console.completer.ArgumentCompleter.ArgumentList;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EscapeAwareWhiteSpaceArgumentDelimiter}.
 *
 * @author Phillip Webb
 */
public class EscapeAwareWhiteSpaceArgumentDelimiterTests {

	private final EscapeAwareWhiteSpaceArgumentDelimiter delimiter = new EscapeAwareWhiteSpaceArgumentDelimiter();

	@Test
	public void simple() {
		String s = "one two";
		assertThat(this.delimiter.delimit(s, 0).getArguments()).containsExactly("one",
				"two");
		assertThat(this.delimiter.parseArguments(s)).containsExactly("one", "two");
		assertThat(this.delimiter.isDelimiter(s, 2)).isFalse();
		assertThat(this.delimiter.isDelimiter(s, 3)).isTrue();
		assertThat(this.delimiter.isDelimiter(s, 4)).isFalse();
	}

	@Test
	public void escaped() {
		String s = "o\\ ne two";
		assertThat(this.delimiter.delimit(s, 0).getArguments()).containsExactly("o\\ ne",
				"two");
		assertThat(this.delimiter.parseArguments(s)).containsExactly("o ne", "two");
		assertThat(this.delimiter.isDelimiter(s, 2)).isFalse();
		assertThat(this.delimiter.isDelimiter(s, 3)).isFalse();
		assertThat(this.delimiter.isDelimiter(s, 4)).isFalse();
		assertThat(this.delimiter.isDelimiter(s, 5)).isTrue();
	}

	@Test
	public void quoted() {
		String s = "'o ne' 't w o'";
		assertThat(this.delimiter.delimit(s, 0).getArguments()).containsExactly("'o ne'",
				"'t w o'");
		assertThat(this.delimiter.parseArguments(s)).containsExactly("o ne", "t w o");
	}

	@Test
	public void doubleQuoted() {
		String s = "\"o ne\" \"t w o\"";
		assertThat(this.delimiter.delimit(s, 0).getArguments())
				.containsExactly("\"o ne\"", "\"t w o\"");
		assertThat(this.delimiter.parseArguments(s)).containsExactly("o ne", "t w o");
	}

	@Test
	public void nestedQuotes() {
		String s = "\"o 'n''e\" 't \"w o'";
		assertThat(this.delimiter.delimit(s, 0).getArguments())
				.containsExactly("\"o 'n''e\"", "'t \"w o'");
		assertThat(this.delimiter.parseArguments(s)).containsExactly("o 'n''e",
				"t \"w o");
	}

	@Test
	public void escapedQuotes() {
		String s = "\\'a b";
		ArgumentList argumentList = this.delimiter.delimit(s, 0);
		assertThat(argumentList.getArguments()).isEqualTo(new String[] { "\\'a", "b" });
		assertThat(this.delimiter.parseArguments(s)).containsExactly("'a", "b");
	}

	@Test
	public void escapes() {
		String s = "\\ \\\\.\\\\\\t";
		assertThat(this.delimiter.parseArguments(s)).containsExactly(" \\.\\\t");
	}

}
