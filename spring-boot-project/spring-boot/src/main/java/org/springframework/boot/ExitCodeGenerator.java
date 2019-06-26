

package org.springframework.boot;

/**
 * Interface used to generate an 'exit code' from a running command line
 * {@link SpringApplication}. Can be used on exceptions as well as directly on beans.
 *
 * @author Dave Syer
 * @see SpringApplication#exit(org.springframework.context.ApplicationContext,
 * ExitCodeGenerator...)
 */
@FunctionalInterface
public interface ExitCodeGenerator {

	/**
	 * Returns the exit code that should be returned from the application.
	 * @return the exit code.
	 */
	int getExitCode();

}
