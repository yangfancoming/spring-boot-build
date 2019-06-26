

package org.springframework.boot.test.context.example.scan.sub;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootConfigurationFinderTests;

/**
 * Example config used in {@link SpringBootConfigurationFinderTests}. Should not be found
 * since scanner should only search upwards.
 *
 * @author Phillip Webb
 */
@SpringBootConfiguration
public class SubExampleConfig {

}
