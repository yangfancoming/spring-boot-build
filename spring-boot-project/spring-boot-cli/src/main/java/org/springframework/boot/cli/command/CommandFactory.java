

package org.springframework.boot.cli.command;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * Factory used to create CLI {@link Command}s. Intended for use with a Java
 * {@link ServiceLoader}.
 *
 * @author Dave Syer
 */
@FunctionalInterface
public interface CommandFactory {

	/**
	 * Returns the CLI {@link Command}s.
	 * @return the commands
	 */
	Collection<Command> getCommands();

}
