package de.workshops.bookshelf.books;

import jakarta.validation.constraints.NotBlank;

public record BookSearchRequest(String title, @NotBlank String author) {
}