package com.ebi.library_management_system.entity;

import java.sql.Date;

public record Book(int id, String title, Author author, Date publishedAt) {
  public Book(int id) {
    this(id, null, null, null);
  }
}
