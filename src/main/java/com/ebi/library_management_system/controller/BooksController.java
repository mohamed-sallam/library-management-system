package com.ebi.library_management_system.controller;

import com.ebi.library_management_system.model.dto.BookAdditionDto;
import com.ebi.library_management_system.model.dto.BookResponseDto;
import com.ebi.library_management_system.model.dto.BorrowingPayloadDto;
import com.ebi.library_management_system.model.dto.ReturningPayloadDto;
import com.ebi.library_management_system.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class BooksController {
    private final BooksService service;

    @GetMapping("books")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        Response<List<BookResponseDto>> response = service.getAllBooks();
        if (response.isSuccess())
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("book")
    public ResponseEntity<?> addBook(@RequestBody BookAdditionDto book) {
        Response<Void> response = service.addBook(book);
        if (response.isSuccess())
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("book/{bookId}")
    public ResponseEntity<?> removeBook(@PathVariable int bookId) {
        Response<Boolean> response = service.removeBook(bookId);
        if (response.isSuccess()) {
            if (response.get())
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("author")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@RequestParam(name = "authorName") String authorName) {
        Response<List<BookResponseDto>> response = service.getBooksByAuthor(authorName);
        if (response.isSuccess())
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        return new ResponseEntity<>(response.get(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("book")
    public ResponseEntity<List<BookResponseDto>> getBooksByTitle(@RequestParam(name = "title") String title) {
        Response<List<BookResponseDto>> response = service.getBooksByTitle(title);
        if (response.isSuccess())
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        return new ResponseEntity<>(response.get(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("borrow")
    public ResponseEntity<?> borrowBook(@RequestBody BorrowingPayloadDto borrowingPayloadDto) {
        Response<Boolean> response = service.borrowBook(borrowingPayloadDto);
        if (response.get())
            return new ResponseEntity<>(HttpStatus.OK);
        if (!response.isSuccess())
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response.message(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("return")
    public ResponseEntity<?> returnBook(@RequestBody ReturningPayloadDto returningPayloadDto) {
        Response<Boolean> response = service.returnBook(returningPayloadDto);
        if (response.get())
            return new ResponseEntity<>(response.message(), HttpStatus.OK);
        return new ResponseEntity<>(response.message(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
