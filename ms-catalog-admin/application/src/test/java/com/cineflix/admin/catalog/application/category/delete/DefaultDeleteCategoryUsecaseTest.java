package com.cineflix.admin.catalog.application.category.delete;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DefaultDeleteCategoryUsecaseTest {
    @InjectMocks
    private DefaultDeleteCategoryUsecase usecase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenAValidId_WhenCallsExecute_shouldBeOk() {
        final var category = Category.newCategory("Films", "Most watched", true);
        final var expectedId = category.getId();

        Mockito.doNothing().when(gateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> usecase.execute(category.getId().getValue()));

        Mockito.verify(gateway, Mockito.atMostOnce()).deleteById(expectedId);
    }

    @Test
    public void givenAInvalidId_WhenCallsExecute_shouldBeOk() {
        final var id = CategoryId.from("123");

        Mockito.doNothing().when(gateway).deleteById(Mockito.eq(id));

        Assertions.assertDoesNotThrow(() -> usecase.execute(id.getValue()));

        Mockito.verify(gateway, Mockito.atMostOnce()).deleteById(id);
    }

    @Test
    public void givenAValidId_WhenGatewayThrowsError_shouldReturnException() {
        final var id = CategoryId.from("123");

        Mockito.doThrow(new IllegalStateException()).when(gateway).deleteById(Mockito.eq(id));

        Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(id.getValue()))  ;

        Mockito.verify(gateway, Mockito.atMostOnce()).deleteById(id);
    }
}
