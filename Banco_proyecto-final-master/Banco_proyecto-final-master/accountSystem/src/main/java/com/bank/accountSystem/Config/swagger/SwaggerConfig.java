package com.bank.accountSystem.Config.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI BankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Project Bank API")
                        .description("Project bank application for Jean viña de mares and Paula Andrea")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://github/paula.html")));

    }


}
