

package org.springframework.boot.web.server;

import org.apache.coyote.http11.Http11NioProtocol;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.testsupport.runner.classpath.ModifiedClassPathRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Compression}.
 *
 * @author Andy Wilkinson
 */
@RunWith(ModifiedClassPathRunner.class)
public class CompressionTests {

	@Test
	public void defaultCompressableMimeTypesMatchesTomcatsDefault() {
		assertThat(new Compression().getMimeTypes())
				.containsExactlyInAnyOrder(getTomcatDefaultCompressableMimeTypes());
	}

	private String[] getTomcatDefaultCompressableMimeTypes() {
		Http11NioProtocol protocol = new Http11NioProtocol();
		return protocol.getCompressibleMimeTypes();
	}

}
