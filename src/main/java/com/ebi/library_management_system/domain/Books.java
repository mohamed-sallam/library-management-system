package com.ebi.library_management_system.domain;

import com.ebi.library_management_system.entity.Book;

import java.util.List;

public interface Books {
  void add(Book book);
  List<Book> getAll();
  boolean remove(Book book);
  List<Book> getByTitle(String title);
  List<Book> getByAuthor(String author);
  Book getById(int id);
}
