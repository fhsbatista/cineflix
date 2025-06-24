package com.cineflix.admin.catalog.application.category.create;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateCategoryUsecase extends CreateCategoryUsecase {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUsecase(final CategoryGateway categoryGateway) {
        Objects.requireNonNull(categoryGateway);
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand command) {
        final var notification = Notification.create();
        final var category = Category.newCategory(
                command.name(),
                command.description(),
                command.isActive()
        );
        category.validate(notification);

        if (notification.hasErrors()) {
            return Left(notification);
        }

        return Try(() -> categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
