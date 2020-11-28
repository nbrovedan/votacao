package br.com.brovetech.votacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VotacaoAPI")
                        .version("Controle de votação de pautas")
                        .description("Controle de votação de pautas")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi v1() {
        String[] paths = {"/v1/**"};
        String[] packagesScan = {"br.com.brovetech.votacao.resource.v1"};
        return GroupedOpenApi.builder().group("v1").pathsToMatch(paths).packagesToScan(packagesScan).build();
    }
}
