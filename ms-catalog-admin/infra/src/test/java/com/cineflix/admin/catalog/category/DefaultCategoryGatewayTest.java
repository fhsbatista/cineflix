package com.cineflix.admin.catalog.category;

import com.cineflix.admin.catalog.category.persistence.CategoryRepository;
import com.cineflix.admin.catalog.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wildfly.common.Assert;

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
}
