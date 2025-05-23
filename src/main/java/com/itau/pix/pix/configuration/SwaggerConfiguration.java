package com.itau.pix.pix.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("API PIX")
                    .version("1.0")
                    .description("API para gerenciamento de chaves PIX"))
            .servers(List.of(
                    new Server()
                            .url("/api/v1")
                            .description("Servidor Local")
            ));
  }
}