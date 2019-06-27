

package org.springframework.boot.json;

import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Thin wrapper to adapt {@link org.json.simple.JSONObject} to a {@link JsonParser}.
 *
 * @author Dave Syer
 * @author Jean de Klerk
 * @since 1.2.0
 * @see JsonParserFactory
 */
public class JsonSimpleJsonParser extends AbstractJsonParser {

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> parseMap(String json) {
		return (Map<String, Object>) tryParse(() -> new JSONParser().parse(json),
				ParseException.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> parseList(String json) {
		return (List<Object>) tryParse(() -> new JSONParser().parse(json),
				ParseException.class);
	}

}
