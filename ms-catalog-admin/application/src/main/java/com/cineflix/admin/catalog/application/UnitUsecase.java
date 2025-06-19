package com.cineflix.admin.catalog.application;

public abstract class UnitUsecase<IN> {
    public abstract void execute(IN input);
}
