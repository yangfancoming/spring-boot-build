

package org.springframework.boot.actuate.autoconfigure.endpoint.web;

import java.util.Collections;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MappingWebEndpointPathMapper}.
 *
 * @author Stephane Nicoll
 */
public class MappingWebEndpointPathMapperTests {

	@Test
	public void defaultConfiguration() {
		MappingWebEndpointPathMapper mapper = new MappingWebEndpointPathMapper(
				Collections.emptyMap());
		assertThat(mapper.getRootPath("test")).isEqualTo("test");
	}

	@Test
	public void userConfiguration() {
		MappingWebEndpointPathMapper mapper = new MappingWebEndpointPathMapper(
				Collections.singletonMap("test", "custom"));
		assertThat(mapper.getRootPath("test")).isEqualTo("custom");
	}

}
