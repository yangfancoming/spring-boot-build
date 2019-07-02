

package org.springframework.boot.test.autoconfigure.web.reactive.webclient;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;

/**
 * An example attribute that requires a {@link Converter}.
 *
 * @author Stephane Nicoll
 */
public class ExampleId {

	private final UUID id;

	ExampleId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return this.id;
	}

}
