package com.cineflix.admin.catalog.domain.validation;

import com.cineflix.admin.catalog.domain.category.Category;

public class CategoryValidator extends Validator {
    private final Category category;

    public CategoryValidator(Category category, ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        final var name = category.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isEmpty()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be blank"));
            return;
        }

        final var nameLength = name.length();
        if (nameLength < 3 || nameLength > 255) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 chars"));
        }
    }
}
