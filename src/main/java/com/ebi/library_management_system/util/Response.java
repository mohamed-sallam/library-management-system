package com.ebi.library_management_system.util;

public record Response<T>(
    T get,
    boolean isSuccess,
    String message
) {
}
