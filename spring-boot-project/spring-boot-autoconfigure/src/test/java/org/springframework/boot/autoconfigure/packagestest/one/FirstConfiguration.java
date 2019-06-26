

package org.springframework.boot.autoconfigure.packagestest.one;

import org.springframework.boot.autoconfigure.AutoConfigurationPackagesTests;
import org.springframework.boot.autoconfigure.AutoConfigurationPackagesTests.TestRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Sample configuration used in {@link AutoConfigurationPackagesTests}.
 *
 * @author Oliver Gierke
 */
@Configuration
@Import(TestRegistrar.class)
public class FirstConfiguration {

}
