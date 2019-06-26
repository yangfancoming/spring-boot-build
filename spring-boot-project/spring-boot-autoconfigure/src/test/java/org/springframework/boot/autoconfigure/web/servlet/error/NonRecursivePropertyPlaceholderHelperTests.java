

package org.springframework.boot.autoconfigure.web.servlet.error;

import java.util.Properties;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NonRecursivePropertyPlaceholderHelper}.
 *
 * @author Phillip Webb
 */
public class NonRecursivePropertyPlaceholderHelperTests {

	private final NonRecursivePropertyPlaceholderHelper helper = new NonRecursivePropertyPlaceholderHelper(
			"${", "}");

	@Test
	public void canResolve() {
		Properties properties = new Properties();
		properties.put("a", "b");
		String result = this.helper.replacePlaceholders("${a}", properties);
		assertThat(result).isEqualTo("b");
	}

	@Test
	public void cannotResolveRecursive() {
		Properties properties = new Properties();
		properties.put("a", "${b}");
		properties.put("b", "c");
		String result = this.helper.replacePlaceholders("${a}", properties);
		assertThat(result).isEqualTo("${b}");
	}

}
