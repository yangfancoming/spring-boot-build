

package org.springframework.boot.autoconfigure.template;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import org.springframework.util.MimeTypeUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AbstractViewResolverProperties}.
 *
 * @author Stephane Nicoll
 */
public class ViewResolverPropertiesTests {

	@Test
	public void defaultContentType() {
		assertThat(new ViewResolverProperties().getContentType())
				.hasToString("text/html;charset=UTF-8");
	}

	@Test
	public void customContentTypeDefaultCharset() {
		ViewResolverProperties properties = new ViewResolverProperties();
		properties.setContentType(MimeTypeUtils.parseMimeType("text/plain"));
		assertThat(properties.getContentType()).hasToString("text/plain;charset=UTF-8");
	}

	@Test
	public void defaultContentTypeCustomCharset() {
		ViewResolverProperties properties = new ViewResolverProperties();
		properties.setCharset(StandardCharsets.UTF_16);
		assertThat(properties.getContentType()).hasToString("text/html;charset=UTF-16");
	}

	@Test
	public void customContentTypeCustomCharset() {
		ViewResolverProperties properties = new ViewResolverProperties();
		properties.setContentType(MimeTypeUtils.parseMimeType("text/plain"));
		properties.setCharset(StandardCharsets.UTF_16);
		assertThat(properties.getContentType()).hasToString("text/plain;charset=UTF-16");
	}

	@Test
	public void customContentTypeWithPropertyAndCustomCharset() {
		ViewResolverProperties properties = new ViewResolverProperties();
		properties.setContentType(MimeTypeUtils.parseMimeType("text/plain;foo=bar"));
		properties.setCharset(StandardCharsets.UTF_16);
		assertThat(properties.getContentType())
				.hasToString("text/plain;charset=UTF-16;foo=bar");
	}

	private static class ViewResolverProperties extends AbstractViewResolverProperties {

	}

}
