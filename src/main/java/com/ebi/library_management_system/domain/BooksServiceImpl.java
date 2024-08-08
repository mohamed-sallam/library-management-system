package com.ebi.library_management_system.domain;

import com.ebi.library_management_system.entity.Book;
import com.ebi.library_management_system.entity.Copy;
import com.ebi.library_management_system.entity.Customer;
import com.ebi.library_management_system.model.dto.BookAdditionDto;
import com.ebi.library_management_system.model.dto.BookResponseDto;
import com.ebi.library_management_system.model.dto.BorrowingPayloadDto;
import com.ebi.library_management_system.model.dto.ReturningPayloadDto;
import com.ebi.library_management_system.util.Response;
import com.ebi.library_management_system.util.mapper.BookMapper;
import com.ebi.library_management_system.util.mapper.CopyMapper;
import com.ebi.library_management_system.util.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {
  private final Books booksRepo;
  private final Copies copiesRepo;

  @Override
  public Response<Void> addBook(BookAdditionDto book) {
    try {
      booksRepo.add(BookMapper.toEntity(book));
      return new Response<>(null, true, "Great, the book has been added!");
    } catch (RuntimeException e) {
      return new Response<>(null, false, "Something went wrong!");
    }
  }

  @Override
  public Response<List<BookResponseDto>> getAllBooks() {
    try {
      return new Response<>(booksRepo.getAll().stream().map(BookMapper::toDto).toList(), true, "Great, here are the books!");
    } catch (RuntimeException e) {
      return new Response<>(new ArrayList<>(), false, "Something went wrong!");
    }
  }

  @Override
  public Response<Boolean> removeBook(int bookId) {
    boolean isRemoved;
    try {
      isRemoved = booksRepo.remove(BookMapper.toEntity(bookId));
    } catch (RuntimeException e) {
      return new Response<>(false, false, "Something went wrong!");
    }

    return (isRemoved) ?
        new Response<>(true, true, "Great, the bookId has been removed!") :
        new Response<>(false, true, "The bookId doesn't exist!");
  }

  @Override
  public Response<List<BookResponseDto>> getBooksByTitle(String title) {
    try {
      return new Response<>(booksRepo.getByTitle(title).stream().map(BookMapper::toDto).toList(), true, "Great, here are the books!");
    } catch (RuntimeException e) {
      return new Response<>(new ArrayList<>(), false, "Something went wrong!");
    }
  }

  @Override
  public Response<List<BookResponseDto>> getBooksByAuthor(String author) {
    try {
      return new Response<>(booksRepo.getByAuthor(author).stream().map(BookMapper::toDto).toList(), true, "Great, here are the books!");
    } catch (RuntimeException e) {
      return new Response<>(new ArrayList<>(), false, "Something went wrong!");
    }
  }

  @Override
  public Response<Boolean> borrowBook(BorrowingPayloadDto borrowing) {
    Customer customer = CustomerMapper.toEntity(borrowing.customerId());
    Book book = BookMapper.toEntity(borrowing.bookId());
    List<Copy> availableCopies = copiesRepo.getAvailable(book);
    if (availableCopies.isEmpty())
      return new Response<>(false, true, "This book is no longer available for borrowing!");
    else if (copiesRepo.getBorrowed(customer).size() >= 5)
      return new Response<>(false, true, "This customer has reached the maximum number of borrowed books!");
    try {
      copiesRepo.borrow(customer, availableCopies.get(0));
      return new Response<>(true, true, customer.username() + " has successfully borrowed " + book.title() + "!");
    } catch (RuntimeException e) {
      return new Response<>(false, false, "Something went wrong!");
    }
  }

  @Override
  public Response<Boolean> returnBook(ReturningPayloadDto returning) {
    Customer customer = CustomerMapper.toEntity(returning.customerId());
    Copy copy = CopyMapper.toEntity(returning.copyId());
    try {
      copiesRepo.returnCopy(customer, copy);
      return new Response<>(true, true,  "The book has been returned successfully!");
    } catch (RuntimeException e) {
      return new Response<>(false, false, "Something went wrong!");
    }
  }
}
