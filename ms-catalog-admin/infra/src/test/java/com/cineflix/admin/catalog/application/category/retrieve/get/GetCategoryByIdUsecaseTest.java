package com.cineflix.admin.catalog.application.category.retrieve.get;

import com.cineflix.admin.catalog.IntegrationTest;
import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.exceptions.DomainException;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryJpaEntity;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@IntegrationTest
public class GetCategoryByIdUsecaseTest {
    @Autowired
    private GetCategoryByIdUsecase usecase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidId_WhenCallsExecute_shouldBeReturnCategory() {
        final var category = Category.newCategory("Films", "Most watched", true);
        final var id = category.getId().getValue();
        repository.saveAndFlush(CategoryJpaEntity.from(category));

        final var result = usecase.execute(id);

        Assertions.assertEquals(id, result.id());
        Assertions.assertEquals(category.getName(), result.name());
        Assertions.assertEquals(category.getDescription(), result.description());
        Assertions.assertEquals(category.isActive(), result.isActive());
        Assertions.assertEquals(truncated(category.getCreatedAt()), truncated(result.createdAt()));
        Assertions.assertEquals(truncated(category.getUpdatedAt()), truncated(result.updatedAt()));
        Assertions.assertEquals(category.getDeletedAt(), result.deletedAt());
    }

    @Test
    public void givenAnInValidId_WhenCallsExecute_shouldReturnNotFound() {
        final var category = Category.newCategory("Films", "Most watched", true);
        final var id = category.getId().getValue();

        final var exception = Assertions.assertThrows(
                DomainException.class,
                () -> usecase.execute(id)
        );

        Assertions.assertEquals(
                "Category with id %s was not found".formatted(category.getId().getValue()),
                exception.getErrors().get(0).message()
        );
    }

    @Test
    public void givenAValidId_WhenGatewayThrowsRandomException_shouldReturnError() {
        final var category = Category.newCategory("Films", "Most watched", true);
        final var id = category.getId().getValue();
        final var expectedError = "Gateway error";

        Mockito.doThrow(new IllegalStateException(expectedError)).when(gateway).findById(category.getId());


        final var result = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(id)) ;

        Assertions.assertEquals(expectedError, result.getMessage());
    }

    private Instant truncated(Instant instant) {
        return instant.truncatedTo(ChronoUnit.SECONDS);
    }
}
