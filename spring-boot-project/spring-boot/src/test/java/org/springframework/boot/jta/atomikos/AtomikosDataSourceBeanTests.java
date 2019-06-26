

package org.springframework.boot.jta.atomikos;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link AtomikosDataSourceBean}.
 *
 * @author Phillip Webb
 */
public class AtomikosDataSourceBeanTests {

	@Test
	public void beanMethods() throws Exception {
		MockAtomikosDataSourceBean bean = spy(new MockAtomikosDataSourceBean());
		bean.setBeanName("bean");
		bean.afterPropertiesSet();
		assertThat(bean.getUniqueResourceName()).isEqualTo("bean");
		verify(bean).init();
		verify(bean, never()).close();
		bean.destroy();
		verify(bean).close();
	}

	@SuppressWarnings("serial")
	private static class MockAtomikosDataSourceBean extends AtomikosDataSourceBean {

		@Override
		public synchronized void init() {
		}

		@Override
		public void close() {
		}

	}

}
