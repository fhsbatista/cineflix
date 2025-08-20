package com.cineflix.admin.catalog.category;

import com.cineflix.admin.catalog.category.persistence.CategoryJpaEntity;
import com.cineflix.admin.catalog.category.persistence.CategoryRepository;
import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import com.cineflix.admin.catalog.domain.category.CategorySearchQuery;
import com.cineflix.admin.catalog.domain.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultCategoryGateway implements CategoryGateway {
    @Autowired
    private final CategoryRepository repository;

    public DefaultCategoryGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public Category update(final Category category) {
        return save(category);
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(CategoryId categoryId) {
        final var id = categoryId.getValue();
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }

    private Category save(Category category) {
        return this.repository.save(CategoryJpaEntity.from(category)).toAggregate();
    }
}
