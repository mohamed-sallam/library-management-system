package com.ebi.library_management_system.controller;

import com.ebi.library_management_system.model.dto.BookAdditionDto;
import com.ebi.library_management_system.model.dto.BookResponseDto;
import com.ebi.library_management_system.model.dto.BorrowingPayloadDto;
import com.ebi.library_management_system.model.dto.ReturningPayloadDto;
import com.ebi.library_management_system.util.Response;

import java.util.List;

public interface BooksService {
  Response<Void> addBook(BookAdditionDto book);
  Response<List<BookResponseDto>> getAllBooks();
  Response<Boolean> removeBook(int bookId);
  Response<List<BookResponseDto>> getBooksByTitle(String title);
  Response<List<BookResponseDto>> getBooksByAuthor(String author);
  Response<Boolean> borrowBook(BorrowingPayloadDto borrowing);
  Response<Boolean> returnBook(ReturningPayloadDto returning);
}
