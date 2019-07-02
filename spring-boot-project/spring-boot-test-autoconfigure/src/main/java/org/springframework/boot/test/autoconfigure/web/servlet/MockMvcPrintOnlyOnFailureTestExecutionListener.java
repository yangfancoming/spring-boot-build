

package org.springframework.boot.test.autoconfigure.web.servlet;

import org.springframework.boot.test.autoconfigure.web.servlet.SpringBootMockMvcBuilderCustomizer.DeferredLinesWriter;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * {@link TestExecutionListener} used to print MVC lines only on failure.
 *
 * @author Phillip Webb
 */
class MockMvcPrintOnlyOnFailureTestExecutionListener
		extends AbstractTestExecutionListener {

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 100;
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		if (testContext.getTestException() != null) {
			DeferredLinesWriter writer = DeferredLinesWriter
					.get(testContext.getApplicationContext());
			if (writer != null) {
				writer.writeDeferredResult();
			}
		}

	}

}
