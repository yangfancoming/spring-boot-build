

package org.springframework.boot.cli.command.archive;

import java.io.File;

import org.springframework.boot.cli.command.Command;
import org.springframework.boot.loader.tools.Layouts;
import org.springframework.boot.loader.tools.LibraryScope;

/**
 * {@link Command} to create a self-contained executable jar file from a CLI application.
 *
 * @author Andy Wilkinson
 * @author Phillip Webb
 */
public class JarCommand extends ArchiveCommand {

	public JarCommand() {
		super("jar", "Create a self-contained executable jar "
				+ "file from a Spring Groovy script", new JarOptionHandler());
	}

	private static final class JarOptionHandler extends ArchiveOptionHandler {

		JarOptionHandler() {
			super("jar", new Layouts.Jar());
		}

		@Override
		protected LibraryScope getLibraryScope(File file) {
			return LibraryScope.COMPILE;
		}

	}

}
