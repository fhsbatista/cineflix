package com.cineflix.admin.catalog.application.category.update;

import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import com.cineflix.admin.catalog.domain.exceptions.DomainException;
import com.cineflix.admin.catalog.domain.validation.handler.Notification;
import com.cineflix.admin.catalog.domain.validation.Error;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;


public class DefaultUpdateCategoryUsecase extends UpdateCategoryUsecase {
    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUsecase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand command) {
        final var id = CategoryId.from(command.id());
        final var name = command.name();
        final var description = command.description();
        final var isActive = command.isActive();
        final var category = categoryGateway.findById(id).orElseThrow(notFound(id));

        final var notification = Notification.create();
        category.update(name, description, isActive).validate(notification);

        if (notification.hasErrors()) {
            return Left(notification);
        }

        return API.Try(() -> categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private Supplier<DomainException> notFound(final CategoryId id) {
        final var error = new Error("Category with id %s was not found".formatted(id.getValue()));
        return () -> DomainException.with(error);
    }
}
