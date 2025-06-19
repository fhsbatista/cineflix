package com.cineflix.admin.catalog.domain.category;

import com.cineflix.admin.catalog.domain.pagination.Pagination;

public interface CategoryGateway {
    Category create(Category category);
    Category update(Category category);
    Category findById(CategoryId id);
    void deleteById(CategoryId id);
    Pagination<Category> findAll(CategorySearchQuery query);
}
