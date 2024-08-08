package com.ebi.library_management_system.domain;

import com.ebi.library_management_system.entity.Book;
import com.ebi.library_management_system.entity.Copy;
import com.ebi.library_management_system.entity.Customer;

import java.util.List;

public interface Copies {
  List<Copy> getAvailable(Book book);
  void borrow(Customer customer, Copy copy);
  boolean returnCopy(Customer customer, Copy copy);
  List<Copy> getBorrowed(Customer customer);
}
