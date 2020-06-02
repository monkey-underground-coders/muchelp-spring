package com.a6raywa1cher.muchelpspring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

//import springfox.documentation.builders.*;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger.web.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
//import static springfox.documentation.builders.PathSelectors.regex;
//import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
//@EnableSwagger2WebMvc
public class SwaggerConfig {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);
	@Value("${app.version}")
	String version;

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(
						new Info().title("muchelp-spring")
								.description("Spring shop sample application")
								.version(version)
								.license(new License().name("MIT License")
										.url("https://github.com/monkey-underground-coders/mucpoll-spring/blob/master/LICENSE")
								)
				)
				.components(
						new Components()
								.addSecuritySchemes("jwt", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
				);
	}

	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}

//	@Bean
//	public GroupedOpenApi publicApi() {
//		return GroupedOpenApi.builder()
//				.group("muchelp-spring-public")
//				.packagesToScan("com.a6raywa1cher.muchelpspring.security.rest")
//				.pathsToMatch("/**")
//				.build();
//	}

//	@Bean
//	public Docket swaggerSpringfoxDocket(TypeResolver resolver) {
//		log.debug("Starting Swagger");
//
//		ApiInfo apiInfo = new ApiInfoBuilder()
//				.title("muchelp-spring")
//				.version(version)
//				.license("MIT License")
//				.licenseUrl("https://github.com/monkey-underground-coders/mucpoll-spring/blob/master/LICENSE")
//				.build();
//
//		Docket docket = new Docket(DocumentationType.SWAGGER_2)
//				.produces(Collections.singleton("application/json"))
//				.consumes(Collections.singleton("application/json"))
//				.apiInfo(apiInfo)
//				.pathMapping("/")
//				.apiInfo(ApiInfo.DEFAULT)
//				.forCodeGeneration(true)
//				.genericModelSubstitutes(ResponseEntity.class)
//				.ignoredParameterTypes(Pageable.class)
//				.ignoredParameterTypes(java.sql.Date.class)
//				.ignoredParameterTypes(User.class)
//				.directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
//				.directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
//				.directModelSubstitute(java.time.LocalDateTime.class, Date.class)
//				.additionalModels(resolver.resolve(User.class))
//				.securityContexts(Collections.singletonList(securityContext()))
//				.securitySchemes(Collections.singletonList(apiKey()))
//				.useDefaultResponseMessages(false);
//
//		docket = docket.select()
//				.apis(RequestHandlerSelectors.basePackage("com.a6raywa1cher.muchelpspring"))
//				.build();
//		return docket;
//	}
//
//
//	private ApiKey apiKey() {
//		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
//	}
//
//	private SecurityContext securityContext() {
//		return SecurityContext.builder()
//				.securityReferences(defaultAuth())
//				.forPaths(Predicate.not(
//						PathSelectors.ant("/auth/get_access")
//				))
//				.build();
//	}
//
//	List<SecurityReference> defaultAuth() {
//		AuthorizationScope authorizationScope
//				= new AuthorizationScope("global", "accessEverything");
//		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//		authorizationScopes[0] = authorizationScope;
//		return Collections.singletonList(
//				new SecurityReference("JWT", authorizationScopes));
//	}

}