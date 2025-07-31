package com.cineflix.admin.catalog.application.category.retrieve.get;

import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import com.cineflix.admin.catalog.domain.exceptions.DomainException;
import com.cineflix.admin.catalog.domain.validation.Error;
import com.cineflix.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.*;

public class DefaultGetCategoryByIdUsecase extends GetCategoryByIdUsecase {
    private CategoryGateway gateway;

    public DefaultGetCategoryByIdUsecase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CategoryOutput execute(String input) {
        final var id = CategoryId.from(input);
        return gateway.findById(id)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(id));
    }

    private Supplier<DomainException> notFound(final CategoryId id) {
        final var error = new Error("Category with id %s was not found".formatted(id.getValue()));
        return () -> DomainException.with(error);
    }
}
