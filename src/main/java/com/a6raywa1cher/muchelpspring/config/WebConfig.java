package com.a6raywa1cher.muchelpspring.config;

import com.a6raywa1cher.muchelpspring.utils.UserHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	private UserHandlerMethodArgumentResolver userHandlerMethodArgumentResolver;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("swagger-ui.html")
//				.addResourceLocations("classpath:/META-INF/resources/");
//
//		registry.addResourceHandler("/webjars/**")
//				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userHandlerMethodArgumentResolver);
	}
}