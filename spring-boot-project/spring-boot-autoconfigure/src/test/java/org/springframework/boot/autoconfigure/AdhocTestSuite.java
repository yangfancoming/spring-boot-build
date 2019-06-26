

package org.springframework.boot.autoconfigure;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfigurationTests;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfigurationTests;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorControllerDirectMockMvcTests;

/**
 * A test suite for probing weird ordering problems in the tests.
 *
 * @author Dave Syer
 */
@RunWith(Suite.class)
@SuiteClasses({ BasicErrorControllerDirectMockMvcTests.class,
		JmxAutoConfigurationTests.class, IntegrationAutoConfigurationTests.class })
@Ignore
public class AdhocTestSuite {

}
