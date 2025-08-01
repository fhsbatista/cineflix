package com.cineflix.admin.catalog.application.category.retrieve.list;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategorySearchQuery;
import com.cineflix.admin.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

public class DefaultListCategoriesUsecaseTest {
    @InjectMocks
    private DefaultListCategoriesUsecase usecase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    void givenAValidQuery_whenCallExecute_thenReturnCategories() {
        final var categories = List.of(
                Category.newCategory("Sports", "Sport movies", true),
                Category.newCategory("Action", "Action movies", true)
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAd";
        final var expectedDirection = "asc";
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var pagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        Mockito.when(gateway.findAll(eq(query))).thenReturn(pagination);

        final var result = usecase.execute(query);

        final var expectedResult = pagination.map(CategoryListOutput::from);
        Assertions.assertEquals(categories.size(), result.items().size());
        Assertions.assertEquals(expectedResult, result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(categories.size(), result.total());
    }

    @Test
    void givenAValidQuery_whenCallExecuteAndHasNoCategories_thenReturnEmpty() {
        final var categories = List.<Category>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAd";
        final var expectedDirection = "asc";
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var pagination = new Pagination<>(expectedPage, expectedPerPage, 0, categories);
        Mockito.when(gateway.findAll(eq(query))).thenReturn(pagination);

        final var result = usecase.execute(query);

        final var expectedResult = pagination.map(CategoryListOutput::from);
        Assertions.assertEquals(0, result.items().size());
        Assertions.assertEquals(expectedResult, result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(0, result.total());
    }

    @Test
    void givenAValidQuery_whenCallExecuteAndGatewayThrowsException_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAd";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Error";
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        Mockito.when(gateway.findAll(eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var result = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, result.getMessage());
    }
}
