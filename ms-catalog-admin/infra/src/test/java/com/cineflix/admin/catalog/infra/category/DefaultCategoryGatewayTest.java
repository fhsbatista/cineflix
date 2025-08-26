package com.cineflix.admin.catalog.infra.category;

import com.cineflix.admin.catalog.GatewayTest;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryJpaEntity;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryRepository;
import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import com.cineflix.admin.catalog.domain.category.CategorySearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var moviesCategory = Category.newCategory("Movies", null, true);
        final var tvShowsCategory = Category.newCategory("Tv shows", null, true);
        final var cartoonCategory = Category.newCategory("Cartoons", null, true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(moviesCategory),
                CategoryJpaEntity.from(tvShowsCategory),
                CategoryJpaEntity.from(cartoonCategory)));

        Assertions.assertEquals(3, repository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var result = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedPerPage, result.items().size());
        Assertions.assertEquals(cartoonCategory.getId(), result.items().get(0).getId());

    }

    @Test
    public void givenNoCategories_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, repository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var result = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedTotal, result.items().size());

    }

    @Test
    public void givenNotFirstPage_whenCallsFindAll_shouldReturnPaginatedAccordinglyToRequestedPage() {
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var moviesCategory = Category.newCategory("Movies", null, true);
        final var tvShowsCategory = Category.newCategory("Tv shows", null, true);
        final var cartoonCategory = Category.newCategory("Cartoons", null, true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(moviesCategory),
                CategoryJpaEntity.from(tvShowsCategory),
                CategoryJpaEntity.from(cartoonCategory)));

        Assertions.assertEquals(3, repository.count());

        final var queryPage0 = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var resultPage0 = gateway.findAll(queryPage0);

        Assertions.assertEquals(0, resultPage0.currentPage());
        Assertions.assertEquals(expectedPerPage, resultPage0.perPage());
        Assertions.assertEquals(expectedTotal, resultPage0.total());
        Assertions.assertEquals(expectedPerPage, resultPage0.items().size());
        Assertions.assertEquals(cartoonCategory.getId(), resultPage0.items().get(0).getId());

        final var queryPage1 = new CategorySearchQuery(1, 1, "", "name", "asc");
        final var resultPage1 = gateway.findAll(queryPage1);

        Assertions.assertEquals(1, resultPage1.currentPage());
        Assertions.assertEquals(expectedPerPage, resultPage1.perPage());
        Assertions.assertEquals(expectedTotal, resultPage1.total());
        Assertions.assertEquals(expectedPerPage, resultPage1.items().size());
        Assertions.assertEquals(moviesCategory.getId(), resultPage1.items().get(0).getId());

        final var queryPage2 = new CategorySearchQuery(2, 1, "", "name", "asc");
        final var resultPage2 = gateway.findAll(queryPage2);

        Assertions.assertEquals(2, resultPage2.currentPage());
        Assertions.assertEquals(expectedPerPage, resultPage2.perPage());
        Assertions.assertEquals(expectedTotal, resultPage2.total());
        Assertions.assertEquals(expectedPerPage, resultPage2.items().size());
        Assertions.assertEquals(tvShowsCategory.getId(), resultPage2.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndFilter_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginatedFiltered() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var moviesCategory = Category.newCategory("Movies", null, true);
        final var tvShowsCategory = Category.newCategory("Tv shows", null, true);
        final var cartoonCategory = Category.newCategory("Cartoons", null, true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(moviesCategory),
                CategoryJpaEntity.from(tvShowsCategory),
                CategoryJpaEntity.from(cartoonCategory)));

        Assertions.assertEquals(3, repository.count());

        final var query = new CategorySearchQuery(0, 1, "tv", "name", "asc");
        final var result = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedTotal, result.items().size());
        Assertions.assertEquals(tvShowsCategory.getId(), result.items().get(0).getId());

    }

    @Test
    public void givenPrePersistedCategoriesAndFilter_whenCallsFindAllAndTermsMatchesCategoryDescription_shouldReturnPaginatedFiltered() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var moviesCategory = Category.newCategory("Movies", "Most watched category", true);
        final var tvShowsCategory = Category.newCategory("Tv shows", "Tv classics", true);
        final var cartoonCategory = Category.newCategory("Cartoons", "For kids", true);

        Assertions.assertEquals(0, repository.count());

        repository.saveAll(List.of(
                CategoryJpaEntity.from(moviesCategory),
                CategoryJpaEntity.from(tvShowsCategory),
                CategoryJpaEntity.from(cartoonCategory)));

        Assertions.assertEquals(3, repository.count());

        final var query = new CategorySearchQuery(0, 1, "tv", "name", "asc");
        final var result = gateway.findAll(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedTotal, result.items().size());
        Assertions.assertEquals(tvShowsCategory.getId(), result.items().get(0).getId());

    }

}
