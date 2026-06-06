package com.leaf.api_leaf.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Leaf Ltd — Business Management API")
                        .version("1.0.0")
                        .description("""
                    REST API for managing employee attendance, delivery sheets,
                    and warehouse damaged products.
                    
                    ## Authentication
                    This API uses **JWT Bearer tokens** for authentication.
                    To get a token, use the `/api/auth/login` endpoint.
                    Then click the **Authorize** button and enter: `Bearer <your_token>`
                    
                    ## Roles
                    | Role | Access |
                    |------|--------|
                    | BOSS | Full access to all modules |
                    | STORE | Attendance and delivery sheets |
                    | WAREHOUSE | Attendance and damaged products |
                    | EMPLOYEE | Own attendance only |
                    """)
                        .contact(new Contact()
                                .name("Leaf Ltd")
                                .email("admin@leafltd.com"))
                        .license(new License()
                                .name("MIT License")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                .name("Bearer Auth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT token. Example: Bearer eyJhbGci...")));
    }
}