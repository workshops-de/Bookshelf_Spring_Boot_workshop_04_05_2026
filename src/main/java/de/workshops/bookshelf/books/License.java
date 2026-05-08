package de.workshops.bookshelf.books;

import lombok.Data;

import java.net.URL;

@Data
public class License {
    String name;
    URL url;
}