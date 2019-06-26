

package org.springframework.boot.json;

/**
 * Tests for {@link GsonJsonParser}.
 *
 * @author Dave Syer
 */
public class GsonJsonParserTests extends AbstractJsonParserTests {

	@Override
	protected JsonParser getParser() {
		return new GsonJsonParser();
	}

}
