package com.a6raywa1cher.muchelpspring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@OpenAPIDefinition(info = @Info(title = "My App",
//		description = "Some long and useful description", version = "v1"))
//@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OPENIDCONNECT,
//		flows = @OAuthFlows(
//				authorizationCode = @OAuthFlow(
//						authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}"
//						, tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", scopes = {
//						@OAuthScope(name = "email", description = "email scope"),
//						@OAuthScope(name = "profile", description = "profile scope")})
//		))
public class SwaggerConfig {
	@Value("${springdoc.oAuthFlow.authorizationUrl}")
	String authorizationUrl;
	@Value("${springdoc.oAuthFlow.tokenUrl}")
	String tokenUrl;

	@Bean
	public OpenAPI customOpenAPI() {
		Scopes scopes = new Scopes();
		scopes.addString("email", "email");
		scopes.addString("profile", "profile");
		OAuthFlow oAuthFlow = new OAuthFlow();
		oAuthFlow.authorizationUrl(authorizationUrl);
		oAuthFlow.tokenUrl(tokenUrl);
		oAuthFlow.setScopes(scopes);
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList("oauth"))
				.components(
						new Components()
								.addSecuritySchemes("oauth", new SecurityScheme()
										.type(SecurityScheme.Type.OAUTH2)
										.in(SecurityScheme.In.COOKIE)
										.openIdConnectUrl("http://localhost:8080/login/oauth2/code/google")
										.flows(
												new OAuthFlows().implicit(oAuthFlow)
										)
								));
	}
}