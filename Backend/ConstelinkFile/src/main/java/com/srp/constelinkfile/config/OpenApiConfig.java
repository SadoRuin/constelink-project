package com.srp.constelinkfile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
		Info info = new Info()
			.title("SWAGGER")
			.version(springdocVersion)
			.description("API에 대한 설명 부분");

		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}