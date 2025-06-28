package com.cineflix.admin.catalog.application.category.update;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryId;

public record UpdateCategoryOutput(
        CategoryId id
) {
    public static UpdateCategoryOutput from(Category category) {
        return new UpdateCategoryOutput(category.getId());
    }
}
