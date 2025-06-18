package com.cineflix.admin.catalog.domain;

public abstract class AggregateRoot<Id extends Identifier> extends Entity<Id>{
    protected AggregateRoot(final Id id) {
        super(id);
    }
}
