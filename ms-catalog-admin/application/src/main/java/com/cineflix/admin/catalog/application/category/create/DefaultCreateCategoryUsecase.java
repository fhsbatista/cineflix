package com.cineflix.admin.catalog.application.category.create;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;

public class DefaultCreateCategoryUsecase extends CreateCategoryUsecase {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUsecase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand command) {
        final var category = Category.newCategory(
                command.name(),
                command.description(),
                command.isActive()
        );
        category.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(categoryGateway.create(category));
    }
}
