package com.ebi.library_management_system.model.dto;

import java.sql.Date;

public record BookResponseDto(String title, int authorId, Date publishedAt) {
}
