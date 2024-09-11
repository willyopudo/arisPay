package org.arispay.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Value("${custom.arispay.openapi.dev-url}")
    private String devUrl;

    @Value("${custom.arispay.openapi.prod-url}")
    private String prodUrl;

    @Value("${custom.arispay.api.version}")
    private String apiVersion;

    @Bean
    public OpenAPI myOpenAPI() {
        //final String securitySchemeName = "bearerAuth";
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("admin@arispay.co.ke");
        contact.setName("ArisAdmin");
        contact.setUrl("https://www.arispay.co.ke");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("ArisPay All APIs")
                .version(apiVersion)
                .contact(contact)
                .description("This API exposes endpoints to post payment notifications and manage customer payments data").termsOfService("https://www.arispay.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
