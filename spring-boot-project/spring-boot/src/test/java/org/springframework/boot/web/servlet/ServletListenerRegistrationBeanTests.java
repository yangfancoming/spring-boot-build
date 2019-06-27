

package org.springframework.boot.web.servlet;

import java.util.EventListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ServletListenerRegistrationBean}.
 *
 * @author Dave Syer
 */
public class ServletListenerRegistrationBeanTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private ServletContextListener listener;

	@Mock
	private ServletContext servletContext;

	@Before
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void startupWithDefaults() throws Exception {
		ServletListenerRegistrationBean<ServletContextListener> bean = new ServletListenerRegistrationBean<>(
				this.listener);
		bean.onStartup(this.servletContext);
		verify(this.servletContext).addListener(this.listener);
	}

	@Test
	public void disable() throws Exception {
		ServletListenerRegistrationBean<ServletContextListener> bean = new ServletListenerRegistrationBean<>(
				this.listener);
		bean.setEnabled(false);
		bean.onStartup(this.servletContext);
		verify(this.servletContext, never())
				.addListener(any(ServletContextListener.class));
	}

	@Test
	public void cannotRegisterUnsupportedType() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Listener is not of a supported type");
		new ServletListenerRegistrationBean<EventListener>(new EventListener() {
		});
	}

}
