package com.vorova.todo.webapp.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Todo")
                .version("1.0.0")
                .contact(
                        new Contact()
                                .email("vvvorava@gmail.com")
                                .name("Vladislav")
                                .url("voroba.ru")
                );
        return new OpenAPI().info(info);
    }
    
}
