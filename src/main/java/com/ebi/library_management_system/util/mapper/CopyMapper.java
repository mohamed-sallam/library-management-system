package com.ebi.library_management_system.util.mapper;

import com.ebi.library_management_system.entity.Copy;

public interface CopyMapper {
    static Copy toEntity(int i) {
        return new Copy(i);
    }
}
