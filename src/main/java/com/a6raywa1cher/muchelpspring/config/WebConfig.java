package com.a6raywa1cher.muchelpspring.config;

import com.a6raywa1cher.muchelpspring.utils.UserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	private final UserHandlerMethodArgumentResolver userHandlerMethodArgumentResolver;

	public WebConfig(UserHandlerMethodArgumentResolver userHandlerMethodArgumentResolver) {
		this.userHandlerMethodArgumentResolver = userHandlerMethodArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userHandlerMethodArgumentResolver);
	}
}