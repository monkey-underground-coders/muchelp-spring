package com.a6raywa1cher.muchelpspring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("oidc-user-service")
	private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;

	@Autowired
	@Qualifier("oauth2-user-service")
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	@Qualifier("user-details-service-impl")
	private UserDetailsService userDetailsService;

	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/");

		http.sessionManagement()
				.sessionFixation()
				.migrateSession();

		http
				.csrf().disable()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
//				.exceptionHandling(e -> e
//						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//				)
				.authorizeRequests()
//				.antMatchers("/").permitAll()
//				.antMatchers("/user/reg", "/oauth2/**", "/error").permitAll()
//				.antMatchers("/v2/api-docs", "/webjars/**", "/swagger-resources", "/swagger-resources/**",
//						"/swagger-ui.html").permitAll()
//				.antMatchers("/csrf").permitAll()
//				.antMatchers("/poll").permitAll()
//				.anyRequest().authenticated()
				.antMatchers("/auth/**").authenticated()
				.anyRequest().permitAll();
		http.oauth2Login()
//				.successHandler(customAuthenticationSuccessHandler)
				.userInfoEndpoint()
				.oidcUserService(oidcUserService)
				.userService(oAuth2UserService)
				.and()
//				.redirectionEndpoint()
//				.and()
//				.failureHandler((request, response, exception) -> {
//					request.getSession().setAttribute("error.message", exception.getMessage());
//					handler.onAuthenticationFailure(request, response, exception);
//				})
				.tokenEndpoint()
				.accessTokenResponseClient(accessTokenResponseClient());
		http.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/");
		http.oauth2Client();
		http.cors()
				.configurationSource(corsConfigurationSource());
	}

	@Bean
	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
		DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
				new DefaultAuthorizationCodeTokenResponseClient();
		accessTokenResponseClient.setRequestEntityConverter(new OAuth2AuthorizationCodeGrantRequestEntityConverter());

		OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
				new OAuth2AccessTokenResponseHttpMessageConverter();
		tokenResponseHttpMessageConverter.setTokenResponseConverter(map -> {
			String accessToken = map.get(OAuth2ParameterNames.ACCESS_TOKEN);
			long expiresIn = Long.parseLong(map.get(OAuth2ParameterNames.EXPIRES_IN));

			OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;

			Map<String, Object> additionalParameters = new HashMap<>();

			map.forEach(additionalParameters::put);

			return OAuth2AccessTokenResponse.withToken(accessToken)
					.tokenType(accessTokenType)
					.expiresIn(expiresIn)
					.additionalParameters(additionalParameters)
					.build();
		});
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
				new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

		accessTokenResponseClient.setRestOperations(restTemplate);
		return accessTokenResponseClient;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PATCH", "PUT", "HEAD", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
