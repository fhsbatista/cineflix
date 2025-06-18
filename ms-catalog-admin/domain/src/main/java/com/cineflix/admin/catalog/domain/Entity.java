package com.cineflix.admin.catalog.domain;

import java.util.Objects;

public abstract class Entity<Id extends Identifier> {
    protected final Id id;

    protected Entity(final Id id) {
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
