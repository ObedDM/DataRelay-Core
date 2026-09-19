package com.datarelay.core.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration 
public class OpenApiConfig {

    @Bean 
    public OpenAPI customOpenAPI() {
        final String cookieAuthName = "cookieAuth";
        return new OpenAPI()
            .info(new Info()
                .title("Data Relay API")
                .version("v1.0")
                .description("Reactive API & dataset pipeline"))
            .addSecurityItem(new SecurityRequirement().addList(cookieAuthName))
            .components(new Components()
                .addSecuritySchemes(cookieAuthName,
                    new SecurityScheme()
                        .name("AUTH-TOKEN")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE)));
    }
}