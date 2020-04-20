

package org.springframework.boot.cli.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Runtime exception wrapper that defines additional {@link Option}s that are understood
 * by the {@link CommandRunner}.
 *
 * @author Phillip Webb
 */
public class CommandException extends RuntimeException {

	private static final long serialVersionUID = 0L;

	private final EnumSet<Option> options;

	/**
	 * Create a new {@link CommandException} with the specified options.
	 * @param options the exception options
	 */
	public CommandException(Option... options) {
		this.options = asEnumSet(options);
	}

	/**
	 * Create a new {@link CommandException} with the specified options.
	 * @param message the exception message to display to the user
	 * @param options the exception options
	 */
	public CommandException(String message, Option... options) {
		super(message);
		this.options = asEnumSet(options);
	}

	/**
	 * Create a new {@link CommandException} with the specified options.
	 * @param message the exception message to display to the user
	 * @param cause the underlying cause
	 * @param options the exception options
	 */
	public CommandException(String message, Throwable cause, Option... options) {
		super(message, cause);
		this.options = asEnumSet(options);
	}

	/**
	 * Create a new {@link CommandException} with the specified options.
	 * @param cause the underlying cause
	 * @param options the exception options
	 */
	public CommandException(Throwable cause, Option... options) {
		super(cause);
		this.options = asEnumSet(options);
	}

	private EnumSet<Option> asEnumSet(Option[] options) {
		if (options == null || options.length == 0) {
			return EnumSet.noneOf(Option.class);
		}
		return EnumSet.copyOf(Arrays.asList(options));
	}

	/**
	 * Returns a set of options that are understood by the {@link CommandRunner}.
	 * @return the options understood by the runner
	 */
	public Set<Option> getOptions() {
		return Collections.unmodifiableSet(this.options);
	}

	/**
	 * Specific options understood by the {@link CommandRunner}.
	 */
	public enum Option {

		/**
		 * Hide the exception message.
		 */
		HIDE_MESSAGE,

		/**
		 * Print basic CLI usage information.
		 */
		SHOW_USAGE,

		/**
		 * Print the stack-trace of the exception.
		 */
		STACK_TRACE,

		/**
		 * Re-throw the exception rather than dealing with it.
		 */
		RETHROW

	}

}
