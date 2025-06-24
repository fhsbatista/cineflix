package com.cineflix.admin.catalog.application.category.create;

import com.cineflix.admin.catalog.application.Usecase;
import com.cineflix.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUsecase
        extends Usecase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
