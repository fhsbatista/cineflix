package com.cineflix.admin.catalog.application.category.retrieve.list;

import com.cineflix.admin.catalog.domain.category.Category;

import java.time.Instant;

public record CategoryListOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {
    public static CategoryListOutput from(final Category category) {
        return new CategoryListOutput(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getDeletedAt()
        );
    }

}
