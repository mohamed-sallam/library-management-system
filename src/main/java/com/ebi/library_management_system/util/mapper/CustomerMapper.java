package com.ebi.library_management_system.util.mapper;

import com.ebi.library_management_system.entity.Customer;

public interface CustomerMapper {
    static Customer toEntity(int i) {
        return new Customer(i);
    }
}
