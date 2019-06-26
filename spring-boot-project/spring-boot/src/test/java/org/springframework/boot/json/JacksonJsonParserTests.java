

package org.springframework.boot.json;

/**
 * Tests for {@link JacksonJsonParser}.
 *
 * @author Dave Syer
 */
public class JacksonJsonParserTests extends AbstractJsonParserTests {

	@Override
	protected JsonParser getParser() {
		return new JacksonJsonParser();
	}

}
