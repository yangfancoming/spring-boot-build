

package org.springframework.boot.test.autoconfigure.properties;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Example {@link PropertyMapping} annotation for use with {@link PropertyMappingTests}.
 *
 * @author Phillip Webb
 */
@Retention(RetentionPolicy.RUNTIME)
@PropertyMapping
@interface ExampleMapping {

	String exampleProperty();

}
