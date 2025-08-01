package com.cineflix.admin.catalog.application.category.retrieve.list;

import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategorySearchQuery;
import com.cineflix.admin.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUsecase extends ListCategoriesUsecase {
    private final CategoryGateway gateway;

    public DefaultListCategoriesUsecase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }


    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery query) {
        return this.gateway.findAll(query).map(CategoryListOutput::from);
    }
}
