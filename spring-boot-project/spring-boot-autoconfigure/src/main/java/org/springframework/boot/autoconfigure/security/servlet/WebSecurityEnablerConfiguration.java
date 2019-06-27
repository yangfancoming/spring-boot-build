

package org.springframework.boot.autoconfigure.security.servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * If there is a bean of type WebSecurityConfigurerAdapter, this adds the
 * {@link EnableWebSecurity} annotation. This will make sure that the annotation is
 * present with default security auto-configuration and also if the user adds custom
 * security and forgets to add the annotation. If {@link EnableWebSecurity} has already
 * been added or if a bean with name {@value BeanIds#SPRING_SECURITY_FILTER_CHAIN} has
 * been configured by the user, this will back-off.
 *
 * @author Madhura Bhave
 * @since 2.0.0
 */
@ConditionalOnBean(WebSecurityConfigurerAdapter.class)
@ConditionalOnMissingBean(name = BeanIds.SPRING_SECURITY_FILTER_CHAIN)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
public class WebSecurityEnablerConfiguration {

}
