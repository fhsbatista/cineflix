package com.cineflix.admin.catalog.application.category.delete;

import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;

import java.util.Objects;

public class DefaultDeleteCategoryUsecase extends DeleteCategoryUsecase{
    private CategoryGateway gateway;

    public DefaultDeleteCategoryUsecase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(String input) {
        final var categoryId = CategoryId.from(input);
        gateway.deleteById(categoryId);
    }
}
