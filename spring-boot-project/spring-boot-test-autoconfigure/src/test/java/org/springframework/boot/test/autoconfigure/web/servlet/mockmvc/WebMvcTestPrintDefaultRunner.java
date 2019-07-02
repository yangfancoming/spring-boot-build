

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Test runner used for {@link WebMvcTestPrintDefaultIntegrationTests}.
 *
 * @author Phillip Webb
 */
public class WebMvcTestPrintDefaultRunner extends SpringJUnit4ClassRunner {

	public WebMvcTestPrintDefaultRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected Statement methodBlock(FrameworkMethod frameworkMethod) {
		Statement statement = super.methodBlock(frameworkMethod);
		statement = new AlwaysPassStatement(statement);
		OutputCapture outputCapture = new OutputCapture();
		if (frameworkMethod.getName().equals("shouldPrint")) {
			outputCapture.expect(containsString("HTTP Method"));
		}
		else if (frameworkMethod.getName().equals("shouldNotPrint")) {
			outputCapture.expect(not(containsString("HTTP Method")));
		}
		else {
			throw new IllegalStateException("Unexpected test method");
		}
		System.err.println(frameworkMethod.getName());
		return outputCapture.apply(statement, null);
	}

	private static class AlwaysPassStatement extends Statement {

		private final Statement delegate;

		AlwaysPassStatement(Statement delegate) {
			this.delegate = delegate;
		}

		@Override
		public void evaluate() throws Throwable {
			try {
				this.delegate.evaluate();
			}
			catch (AssertionError ex) {
			}
		}

	}

}
