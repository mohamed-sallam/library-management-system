package com.ebi.library_management_system.util.mapper;

import com.ebi.library_management_system.entity.Author;
import com.ebi.library_management_system.entity.Book;
import com.ebi.library_management_system.model.dto.BookAdditionDto;
import com.ebi.library_management_system.model.dto.BookResponseDto;

public interface BookMapper {
    static Book toEntity(int bookId) {
        return new Book(bookId);
    }

    static Book toEntity(BookAdditionDto book) {
        return new Book(-1, book.title(), new Author(book.authorId(), ""), book.publishedAt());
    }

    static BookResponseDto toDto(Book book) {
        return new BookResponseDto(book.title(), book.id(), book.publishedAt());
    }
}
