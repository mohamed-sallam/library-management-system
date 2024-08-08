package com.ebi.library_management_system.entity;

import java.sql.Date;

public record Copy(int id, Book book, Date addedDate) {
  public Copy(int id) {
    this(id, null, null);
  }
}
