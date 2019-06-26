

package org.springframework.boot.jackson;

/**
 * Sample object used for tests.
 *
 * @author Phillip Webb
 */
public final class NameAndAge {

	private final String name;

	private final int age;

	public NameAndAge(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return this.name;
	}

	public int getAge() {
		return this.age;
	}

}
