package com.a6raywa1cher.muchelpspring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SwaggerConfig {
	@Value("${app.version}")
	String version;

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(
						new Info().title("muchelp-spring")
								.description("Website for helping")
								.version(version)
								.license(new License().name("MIT License")
										.url("https://github.com/monkey-underground-coders/mucpoll-spring/blob/master/LICENSE")
								)
				)
				.components(
						new Components()
								.addSecuritySchemes("jwt", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer"))
				);
	}

	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}
}