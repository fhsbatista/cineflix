package com.cineflix.admin.catalog.domain.category;

import com.cineflix.admin.catalog.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryId extends Identifier {
    protected final String value;

    private CategoryId(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CategoryId unique() {
        return from(UUID.randomUUID());
    }

    public static CategoryId from(final String id) {
        return new CategoryId(id);
    }

    public static CategoryId from(final UUID uuid) {
        return new CategoryId(uuid.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryId that = (CategoryId) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
