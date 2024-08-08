package com.ebi.library_management_system.entity;

public record Customer(int id, String username, String email, String phoneNumber) {
  public Customer(int id) {
    this(id, null, null, null);
  }
}
