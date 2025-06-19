package com.cineflix.admin.catalog.application.category.create;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryId;

public record CreateCategoryOutput(
        CategoryId id
) {
    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
