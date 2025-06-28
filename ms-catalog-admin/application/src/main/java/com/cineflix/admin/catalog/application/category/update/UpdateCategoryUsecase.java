package com.cineflix.admin.catalog.application.category.update;

import com.cineflix.admin.catalog.application.Usecase;
import com.cineflix.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUsecase
    extends Usecase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
