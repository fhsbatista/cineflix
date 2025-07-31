package com.cineflix.admin.catalog.application.category.retrieve.get;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class DefaultGetCategoryByIdUsecaseTest {
    @InjectMocks
    private DefaultGetCategoryByIdUsecase usecase;

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
    public void givenAValidId_WhenCallsExecute_shouldBeReturnCategory() {
        final var category = Category.newCategory("Films", "Most watched", true);
        final var id = category.getId().getValue();
        Mockito.when(gateway.findById(category.getId())).thenReturn(Optional.of(category));

        final var result = usecase.execute(id);

        Mockito.verify(gateway, Mockito.atMostOnce()).findById(category.getId());
        Assertions.assertEquals(id, result.id());
        Assertions.assertEquals(category.getName(), result.name());
        Assertions.assertEquals(category.getDescription(), result.description());
        Assertions.assertEquals(category.isActive(), result.isActive());
        Assertions.assertEquals(category.getCreatedAt(), result.createdAt());
        Assertions.assertEquals(category.getUpdatedAt(), result.updatedAt());
        Assertions.assertEquals(category.getDeletedAt(), result.deletedAt());
    }

    @Test
    public void givenAnInValidId_WhenCallsExecute_shouldReturnNotFound() {
        final var category = Category.newCategory("Films", "Most watched", true);
        final var id = category.getId().getValue();
        Mockito.when(gateway.findById(category.getId())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(
                DomainException.class,
                () -> usecase.execute(id)
        );

        Mockito.verify(gateway, Mockito.atMostOnce()).findById(category.getId());
        Assertions.assertEquals(
                "Category with id %s was not found".formatted(category.getId().getValue()),
                exception.getErrors().getFirst().message()
        );
    }

    @Test
    public void givenAValidId_WhenGatewayThrowsRandomException_shouldReturnError() {
        final var category = Category.newCategory("Films", "Most watched", true);
        final var id = category.getId().getValue();
        final var expectedError = "Gateway error";
        Mockito.when(gateway.findById(category.getId())).thenThrow(new IllegalStateException(expectedError));

        final var result = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(id)) ;

        Assertions.assertEquals(expectedError, result.getMessage());
    }

}
