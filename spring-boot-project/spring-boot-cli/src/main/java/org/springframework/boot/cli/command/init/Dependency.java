

package org.springframework.boot.cli.command.init;

/**
 * Provide some basic information about a dependency.
 *
 * @author Stephane Nicoll
 * @since 1.2.0
 */
final class Dependency {

	private final String id;

	private final String name;

	private final String description;

	Dependency(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

}
