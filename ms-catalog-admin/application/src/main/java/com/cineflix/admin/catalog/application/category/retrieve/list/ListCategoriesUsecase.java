package com.cineflix.admin.catalog.application.category.retrieve.list;

import com.cineflix.admin.catalog.application.Usecase;
import com.cineflix.admin.catalog.domain.category.CategorySearchQuery;
import com.cineflix.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUsecase
        extends Usecase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
