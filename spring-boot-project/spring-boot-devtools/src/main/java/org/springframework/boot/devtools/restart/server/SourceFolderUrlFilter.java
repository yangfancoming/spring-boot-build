

package org.springframework.boot.devtools.restart.server;

import java.net.URL;

/**
 * Filter URLs based on a source folder name. Used to match URLs from the running
 * classpath against source folders on a remote system.
 *
 * @author Phillip Webb
 * @since 1.3.0
 * @see DefaultSourceFolderUrlFilter
 */
@FunctionalInterface
public interface SourceFolderUrlFilter {

	/**
	 * Determine if the specified URL matches a source folder.
	 * @param sourceFolder the source folder
	 * @param url the URL to check
	 * @return {@code true} if the URL matches
	 */
	boolean isMatch(String sourceFolder, URL url);

}
