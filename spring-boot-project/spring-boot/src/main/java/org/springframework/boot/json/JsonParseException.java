

package org.springframework.boot.json;

/**
 * {@link IllegalArgumentException} thrown when source JSON is invalid.
 *
 * @author Anton Telechev
 * @author Phillip Webb
 * @since 2.0.1
 */
public class JsonParseException extends IllegalArgumentException {

	public JsonParseException() {
		this(null);
	}

	public JsonParseException(Throwable cause) {
		super("Cannot parse JSON", cause);
	}

}
