

package org.springframework.boot.json;

/**
 * Tests for {@link JsonSimpleJsonParser}.
 *
 * @author Dave Syer
 */
public class JsonSimpleJsonParserTests extends AbstractJsonParserTests {

	@Override
	protected JsonParser getParser() {
		return new JsonSimpleJsonParser();
	}

}
