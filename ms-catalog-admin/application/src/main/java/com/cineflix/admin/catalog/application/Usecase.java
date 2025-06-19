package com.cineflix.admin.catalog.application;

public abstract class Usecase<IN, OUT> {
    public abstract OUT execute(IN input);
}
