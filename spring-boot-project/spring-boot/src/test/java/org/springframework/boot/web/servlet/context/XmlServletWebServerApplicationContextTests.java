

package org.springframework.boot.web.servlet.context;

import javax.servlet.Servlet;

import org.junit.Test;

import org.springframework.boot.web.servlet.server.MockServletWebServerFactory;
import org.springframework.core.io.ClassPathResource;

import static org.mockito.Mockito.verify;

/**
 * Tests for {@link XmlServletWebServerApplicationContext}.
 *
 * @author Phillip Webb
 */
public class XmlServletWebServerApplicationContextTests {

	private static final String PATH = XmlServletWebServerApplicationContextTests.class
			.getPackage().getName().replace('.', '/') + "/";

	private static final String FILE = "exampleEmbeddedWebApplicationConfiguration.xml";

	private XmlServletWebServerApplicationContext context;

	@Test
	public void createFromResource() {
		this.context = new XmlServletWebServerApplicationContext(
				new ClassPathResource(FILE, getClass()));
		verifyContext();
	}

	@Test
	public void createFromResourceLocation() {
		this.context = new XmlServletWebServerApplicationContext(PATH + FILE);
		verifyContext();
	}

	@Test
	public void createFromRelativeResourceLocation() {
		this.context = new XmlServletWebServerApplicationContext(getClass(), FILE);
		verifyContext();
	}

	@Test
	public void loadAndRefreshFromResource() {
		this.context = new XmlServletWebServerApplicationContext();
		this.context.load(new ClassPathResource(FILE, getClass()));
		this.context.refresh();
		verifyContext();
	}

	@Test
	public void loadAndRefreshFromResourceLocation() {
		this.context = new XmlServletWebServerApplicationContext();
		this.context.load(PATH + FILE);
		this.context.refresh();
		verifyContext();
	}

	@Test
	public void loadAndRefreshFromRelativeResourceLocation() {
		this.context = new XmlServletWebServerApplicationContext();
		this.context.load(getClass(), FILE);
		this.context.refresh();
		verifyContext();
	}

	private void verifyContext() {
		MockServletWebServerFactory factory = this.context
				.getBean(MockServletWebServerFactory.class);
		Servlet servlet = this.context.getBean(Servlet.class);
		verify(factory.getServletContext()).addServlet("servlet", servlet);
	}

}
