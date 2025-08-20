package com.cineflix.admin.catalog.category;

import com.cineflix.admin.catalog.category.persistence.CategoryJpaEntity;
import com.cineflix.admin.catalog.category.persistence.CategoryRepository;
import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@GatewayTest
public class DefaultCategoryGatewayTest {
    @Autowired
    private DefaultCategoryGateway gateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, repository.count());

        final var result = gateway.create(category);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(category.getId(), result.getId());
        Assertions.assertEquals(expectedName, result.getName());
        Assertions.assertEquals(expectedDescription, result.getDescription());
        Assertions.assertEquals(expectedIsActive, result.isActive());
        Assertions.assertEquals(category.getCreatedAt(), result.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), result.getUpdatedAt());
        Assertions.assertNull(result.getDeletedAt());

        final var persistedCategory = repository.findById(category.getId().getValue()).orElseThrow();

        Assertions.assertEquals(category.getId().getValue(), persistedCategory.getId());
        Assertions.assertEquals(expectedName, persistedCategory.getName());
        Assertions.assertEquals(expectedDescription, persistedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, persistedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), persistedCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), persistedCategory.getUpdatedAt());
        Assertions.assertNull(result.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnUpdatedCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Old name", null, !expectedIsActive);

        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, repository.count());

        final var updated = category.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var result = gateway.update(updated);

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(updated.getId(), result.getId());
        Assertions.assertEquals(expectedName, result.getName());
        Assertions.assertEquals(expectedDescription, result.getDescription());
        Assertions.assertEquals(expectedIsActive, result.isActive());
        Assertions.assertEquals(updated.getCreatedAt(), result.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(result.getUpdatedAt()));
        Assertions.assertNull(result.getDeletedAt());

        final var persistedCategory = repository.findById(updated.getId().getValue()).orElseThrow();

        Assertions.assertEquals(category.getId().getValue(), persistedCategory.getId());
        Assertions.assertEquals(expectedName, persistedCategory.getName());
        Assertions.assertEquals(expectedDescription, persistedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, persistedCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), persistedCategory.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(result.getUpdatedAt()));
        Assertions.assertNull(result.getDeletedAt());
    }

    @Test
    public void givenAValidCategoryId_whenCallsDelete_shouldDeleteCategory() {
        final var category = Category.newCategory("Movies", "Most watched", true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, repository.count());

        gateway.deleteById(category.getId());

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAnInvalidCategoryId_whenCallsDelete_shouldNotDeleteCategory() {
        Assertions.assertEquals(0, repository.count());

        gateway.deleteById(CategoryId.from("invalid"));

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAValidCategory_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, repository.count());

        final var result = gateway.findById(category.getId()).get();

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(category.getId(), result.getId());
        Assertions.assertEquals(expectedName, result.getName());
        Assertions.assertEquals(expectedDescription, result.getDescription());
        Assertions.assertEquals(expectedIsActive, result.isActive());
        Assertions.assertEquals(category.getCreatedAt(), result.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), result.getUpdatedAt());
        Assertions.assertNull(result.getDeletedAt());
    }

    @Test
    public void givenACategoryIdThatIsNotPersisted_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, repository.count());

        final var result = gateway.findById(CategoryId.from("empty"));

        Assertions.assertTrue(result.isEmpty());
    }
}
