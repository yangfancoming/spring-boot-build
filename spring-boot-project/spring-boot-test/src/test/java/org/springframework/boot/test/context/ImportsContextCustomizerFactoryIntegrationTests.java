

package org.springframework.boot.test.context;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ImportsContextCustomizerFactoryIntegrationTests.ImportedBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ImportsContextCustomizerFactory} and
 * {@link ImportsContextCustomizer}.
 *
 * @author Phillip Webb
 */
@RunWith(SpringRunner.class)
@Import(ImportedBean.class)
public class ImportsContextCustomizerFactoryIntegrationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ImportedBean bean;

	@Test
	public void beanWasImported() {
		assertThat(this.bean).isNotNull();
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testItselfIsNotABean() {
		this.context.getBean(getClass());
	}

	@Component
	static class ImportedBean {

	}

}
