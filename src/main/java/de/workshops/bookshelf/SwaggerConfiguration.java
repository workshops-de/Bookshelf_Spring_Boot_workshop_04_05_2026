package de.workshops.bookshelf;

import de.workshops.bookshelf.books.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
            .info(
                new Info()
                    .title(swaggerProperties.getTitle())
                    .version(swaggerProperties.getVersion())
                    .description("Bookshelf API for up to " + swaggerProperties.getCapacity() + " books")
                    .license(new License()
                        .name(swaggerProperties.getLicense().getName())
                        .url(swaggerProperties.getLicense().getUrl().toString())
                    )
            );
    }
}