package org.sid.exam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankCreditOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion des Credits Bancaires")
                        .version("v1")
                        .description("Documentation OpenAPI des services REST de gestion des clients, credits et remboursements."));
    }
}
