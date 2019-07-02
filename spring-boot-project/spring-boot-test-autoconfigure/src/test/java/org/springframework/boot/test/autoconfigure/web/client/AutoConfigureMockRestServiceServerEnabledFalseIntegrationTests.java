

package org.springframework.boot.test.autoconfigure.web.client;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for {@link AutoConfigureMockRestServiceServer} with {@code enabled=false}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@RestClientTest
@AutoConfigureMockRestServiceServer(enabled = false)
public class AutoConfigureMockRestServiceServerEnabledFalseIntegrationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void mockServerRestTemplateCustomizerShouldNotBeRegistered() {
		this.applicationContext.getBean(MockServerRestTemplateCustomizer.class);
	}

}
