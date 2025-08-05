package com.cineflix.admin.catalog.category;

import com.cineflix.admin.catalog.category.persistence.CategoryRepository;
import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import com.cineflix.admin.catalog.domain.category.CategorySearchQuery;
import com.cineflix.admin.catalog.domain.pagination.Pagination;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultCategoryGateway implements CategoryGateway {
    private CategoryRepository repository;

    public DefaultCategoryGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(Category category) {
        return null;
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(CategoryId id) {

    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }
}
