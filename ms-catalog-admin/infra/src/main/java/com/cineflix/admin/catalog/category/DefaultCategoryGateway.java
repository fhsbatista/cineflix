package com.cineflix.admin.catalog.category;

import com.cineflix.admin.catalog.category.persistence.CategoryJpaEntity;
import com.cineflix.admin.catalog.category.persistence.CategoryRepository;
import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import com.cineflix.admin.catalog.domain.category.CategorySearchQuery;
import com.cineflix.admin.catalog.domain.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.cineflix.admin.catalog.utils.SpecificationUtils.like;

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
    public Optional<Category> findById(final CategoryId id) {
        return repository.findById(id.getValue()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public void deleteById(final CategoryId categoryId) {
        final var id = categoryId.getValue();
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery query) {
        final var specifications = Optional
                .ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                        final Specification<CategoryJpaEntity> nameLike = like("name", str);
                        final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
                        return nameLike.or(descriptionLike);
                })
                .orElse(null);

        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );
        final var pageResult = repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(final Category category) {
        return this.repository.save(CategoryJpaEntity.from(category)).toAggregate();
    }
}
