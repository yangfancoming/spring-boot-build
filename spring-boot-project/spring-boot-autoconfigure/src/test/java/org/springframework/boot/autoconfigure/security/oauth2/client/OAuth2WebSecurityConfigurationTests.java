

package org.springframework.boot.autoconfigure.security.oauth2.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.Filter;

import org.junit.Test;

import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ObjectUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OAuth2WebSecurityConfiguration}.
 *
 * @author Madhura Bhave
 */
public class OAuth2WebSecurityConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	public void securityConfigurerConfiguresOAuth2Login() {
		this.contextRunner
				.withUserConfiguration(ClientRegistrationRepositoryConfiguration.class,
						OAuth2WebSecurityConfiguration.class)
				.run((context) -> {
					ClientRegistrationRepository expected = context
							.getBean(ClientRegistrationRepository.class);
					ClientRegistrationRepository actual = (ClientRegistrationRepository) ReflectionTestUtils
							.getField(getAuthCodeFilters(context).get(0),
									"clientRegistrationRepository");
					assertThat(isEqual(expected.findByRegistrationId("first"),
							actual.findByRegistrationId("first"))).isTrue();
					assertThat(isEqual(expected.findByRegistrationId("second"),
							actual.findByRegistrationId("second"))).isTrue();
				});
	}

	@Test
	public void securityConfigurerBacksOffWhenClientRegistrationBeanAbsent() {
		this.contextRunner
				.withUserConfiguration(TestConfig.class,
						OAuth2WebSecurityConfiguration.class)
				.run((context) -> assertThat(getAuthCodeFilters(context)).isEmpty());
	}

	@Test
	public void configurationRegistersAuthorizedClientServiceBean() {
		this.contextRunner
				.withUserConfiguration(ClientRegistrationRepositoryConfiguration.class,
						OAuth2WebSecurityConfiguration.class)
				.run((context) -> {
					OAuth2AuthorizedClientService bean = context
							.getBean(OAuth2AuthorizedClientService.class);
					OAuth2AuthorizedClientService authorizedClientService = (OAuth2AuthorizedClientService) ReflectionTestUtils
							.getField(getAuthCodeFilters(context).get(0),
									"authorizedClientService");
					assertThat(authorizedClientService).isEqualTo(bean);
				});
	}

	@Test
	public void securityConfigurerBacksOffWhenOtherWebSecurityAdapterPresent() {
		this.contextRunner.withUserConfiguration(TestWebSecurityConfigurerConfig.class,
				OAuth2WebSecurityConfiguration.class).run((context) -> {
					assertThat(getAuthCodeFilters(context)).isEmpty();
					assertThat(context).getBean(OAuth2AuthorizedClientService.class)
							.isNotNull();
				});
	}

	@Test
	public void authorizedClientServiceBeanIsConditionalOnMissingBean() {
		this.contextRunner
				.withUserConfiguration(OAuth2AuthorizedClientServiceConfiguration.class,
						OAuth2WebSecurityConfiguration.class)
				.run((context) -> {
					OAuth2AuthorizedClientService bean = context
							.getBean(OAuth2AuthorizedClientService.class);
					OAuth2AuthorizedClientService authorizedClientService = (OAuth2AuthorizedClientService) ReflectionTestUtils
							.getField(getAuthCodeFilters(context).get(0),
									"authorizedClientService");
					assertThat(authorizedClientService).isEqualTo(bean);
				});
	}

	@SuppressWarnings("unchecked")
	private List<Filter> getAuthCodeFilters(AssertableApplicationContext context) {
		FilterChainProxy filterChain = (FilterChainProxy) context
				.getBean(BeanIds.SPRING_SECURITY_FILTER_CHAIN);
		List<SecurityFilterChain> filterChains = filterChain.getFilterChains();
		List<Filter> filters = (List<Filter>) ReflectionTestUtils
				.getField(filterChains.get(0), "filters");
		List<Filter> oauth2Filters = filters.stream()
				.filter((f) -> f instanceof OAuth2LoginAuthenticationFilter
						|| f instanceof OAuth2AuthorizationRequestRedirectFilter)
				.collect(Collectors.toList());
		return oauth2Filters.stream()
				.filter((f) -> f instanceof OAuth2LoginAuthenticationFilter)
				.collect(Collectors.toList());
	}

	private boolean isEqual(ClientRegistration reg1, ClientRegistration reg2) {
		boolean result = ObjectUtils.nullSafeEquals(reg1.getClientId(),
				reg2.getClientId());
		result = result
				&& ObjectUtils.nullSafeEquals(reg1.getClientName(), reg2.getClientName());
		result = result && ObjectUtils.nullSafeEquals(reg1.getClientSecret(),
				reg2.getClientSecret());
		result = result && ObjectUtils.nullSafeEquals(reg1.getScopes(), reg2.getScopes());
		result = result && ObjectUtils.nullSafeEquals(reg1.getRedirectUriTemplate(),
				reg2.getRedirectUriTemplate());
		result = result && ObjectUtils.nullSafeEquals(reg1.getRegistrationId(),
				reg2.getRegistrationId());
		result = result && ObjectUtils.nullSafeEquals(reg1.getAuthorizationGrantType(),
				reg2.getAuthorizationGrantType());
		result = result && ObjectUtils.nullSafeEquals(
				reg1.getProviderDetails().getAuthorizationUri(),
				reg2.getProviderDetails().getAuthorizationUri());
		result = result && ObjectUtils.nullSafeEquals(
				reg1.getProviderDetails().getUserInfoEndpoint(),
				reg2.getProviderDetails().getUserInfoEndpoint());
		result = result
				&& ObjectUtils.nullSafeEquals(reg1.getProviderDetails().getTokenUri(),
						reg2.getProviderDetails().getTokenUri());
		return result;
	}

	@Configuration
	@EnableWebSecurity
	protected static class TestConfig {

		@Bean
		public TomcatServletWebServerFactory tomcat() {
			return new TomcatServletWebServerFactory(0);
		}

	}

	@Configuration
	@Import(TestConfig.class)
	static class ClientRegistrationRepositoryConfiguration {

		@Bean
		public ClientRegistrationRepository clientRegistrationRepository() {
			List<ClientRegistration> registrations = new ArrayList<>();
			registrations.add(getClientRegistration("first", "http://user-info-uri.com"));
			registrations.add(getClientRegistration("second", "http://other-user-info"));
			return new InMemoryClientRegistrationRepository(registrations);
		}

		private ClientRegistration getClientRegistration(String id, String userInfoUri) {
			ClientRegistration.Builder builder = ClientRegistration
					.withRegistrationId(id);
			builder.clientName("foo").clientId("foo").clientAuthenticationMethod(
					org.springframework.security.oauth2.core.ClientAuthenticationMethod.BASIC)
					.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
					.scope("read").clientSecret("secret")
					.redirectUriTemplate("http://redirect-uri.com")
					.authorizationUri("http://authorization-uri.com")
					.tokenUri("http://token-uri.com").userInfoUri(userInfoUri)
					.userNameAttributeName("login");
			return builder.build();
		}

	}

	@Configuration
	@Import(ClientRegistrationRepositoryConfiguration.class)
	static class TestWebSecurityConfigurerConfig extends WebSecurityConfigurerAdapter {

	}

	@Configuration
	@Import(ClientRegistrationRepositoryConfiguration.class)
	static class OAuth2AuthorizedClientServiceConfiguration {

		@Bean
		public OAuth2AuthorizedClientService testAuthorizedClientService(
				ClientRegistrationRepository clientRegistrationRepository) {
			return new InMemoryOAuth2AuthorizedClientService(
					clientRegistrationRepository);
		}

	}

}
