

package org.springframework.boot.test.autoconfigure.restdocs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Test application used with {@link AutoConfigureRestDocs} tests.
 *
 * @author Andy Wilkinson
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class RestDocsTestApplication {

}
