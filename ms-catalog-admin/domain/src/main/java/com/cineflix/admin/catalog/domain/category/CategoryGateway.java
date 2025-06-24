package com.cineflix.admin.catalog.domain.category;

import com.cineflix.admin.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category category);
    Category update(Category category);
    Optional<Category> findById(CategoryId id);
    void deleteById(CategoryId id);
    Pagination<Category> findAll(CategorySearchQuery query);
}
