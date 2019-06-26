

package org.springframework.boot.web.servlet.testcomponents;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
class TestListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("listenerAttribute", "alpha");

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
