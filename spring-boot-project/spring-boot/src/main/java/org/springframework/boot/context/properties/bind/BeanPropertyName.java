

package org.springframework.boot.context.properties.bind;

/**
 * Internal utility to help when dealing with Java Bean property names.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
abstract class BeanPropertyName {

	private BeanPropertyName() {
	}

	/**
	 * Return the specified Java Bean property name in dashed form.
	 * @param name the source name
	 * @return the dashed from
	 */
	public static String toDashedForm(String name) {
		return toDashedForm(name, 0);
	}

	/**
	 * Return the specified Java Bean property name in dashed form.
	 * @param name the source name
	 * @param start the starting char
	 * @return the dashed from
	 */
	public static String toDashedForm(String name, int start) {
		StringBuilder result = new StringBuilder();
		char[] chars = name.replace("_", "-").toCharArray();
		for (int i = start; i < chars.length; i++) {
			char ch = chars[i];
			if (Character.isUpperCase(ch) && result.length() > 0
					&& result.charAt(result.length() - 1) != '-') {
				result.append("-");
			}
			result.append(Character.toLowerCase(ch));
		}
		return result.toString();
	}

}
