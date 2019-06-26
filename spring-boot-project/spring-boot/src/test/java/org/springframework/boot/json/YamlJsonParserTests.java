

package org.springframework.boot.json;

/**
 * Tests for {@link YamlJsonParser}.
 *
 * @author Dave Syer
 */
public class YamlJsonParserTests extends AbstractJsonParserTests {

	@Override
	protected JsonParser getParser() {
		return new YamlJsonParser();
	}

}
