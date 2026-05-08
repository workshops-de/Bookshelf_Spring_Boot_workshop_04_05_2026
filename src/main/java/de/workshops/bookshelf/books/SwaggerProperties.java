package de.workshops.bookshelf.books;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger")
@Data
public class SwaggerProperties {
    String title;
    String description;
    int capacity;
    String version;

    License license;
}