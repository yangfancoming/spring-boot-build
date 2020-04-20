

package org.springframework.boot.cli.command.init;

import org.apache.http.entity.ContentType;

/**
 * Represent the response of a {@link ProjectGenerationRequest}.
 *
 * @author Stephane Nicoll
 * @since 1.2.0
 */
class ProjectGenerationResponse {

	private final ContentType contentType;

	private byte[] content;

	private String fileName;

	ProjectGenerationResponse(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Return the {@link ContentType} of this instance.
	 * @return the content type
	 */
	public ContentType getContentType() {
		return this.contentType;
	}

	/**
	 * The generated project archive or file.
	 * @return the content
	 */
	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * The preferred file name to use to store the entity on disk or {@code null} if no
	 * preferred value has been set.
	 * @return the file name, or {@code null}
	 */
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
