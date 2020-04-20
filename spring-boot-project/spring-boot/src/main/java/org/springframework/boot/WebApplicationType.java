

package org.springframework.boot;

/**
 * An enumeration of possible types of web application.
 * @since 2.0.0
 */
public enum WebApplicationType {

	NONE, // The application should not run as a web application and should not start an embedded web server.

	SERVLET,// The application should run as a servlet-based web application and should start an  embedded servlet web server.

	REACTIVE // The application should run as a reactive web application and should start an embedded reactive web server.
}
