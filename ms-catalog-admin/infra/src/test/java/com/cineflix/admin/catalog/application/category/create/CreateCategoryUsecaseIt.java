package com.cineflix.admin.catalog.application.category.create;

import com.cineflix.admin.catalog.IntegrationTest;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUsecaseIt {
    @Autowired
    private DefaultCreateCategoryUsecase usecase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenValidCommand_whenCallExecute_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Horror";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var output = usecase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.id());

        final var actualCategory = repository.findById(output.id().getValue()).get();
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenInvalidName_whenCallExecute_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Horror";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, repository.count());

        final var notification = usecase.execute(command).getLeft();

        assertEquals(1, notification.getErrors().size());
        assertEquals(
                "'name' should not be null",
                notification.firstError().message()
        );
        assertEquals(0, repository.count());
        verify(gateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnException() {
        final String expectedName = "Movie";
        final var expectedDescription = "Horror";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, repository.count());

        final var output = usecase.execute(command).get();

        assertEquals(1, repository.count());

        final var actualCategory = repository.findById(output.id().getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final String expectedName = "Movie";
        final var expectedDescription = "Horror";
        final var expectedIsActive = true;
        final var expectedError = "Gateway error";

        final var command = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        doThrow(new IllegalStateException(expectedError))
                        .when(gateway).create(any());

        final var notification = usecase.execute(command).getLeft();

        assertEquals(1, notification.getErrors().size());
        assertEquals(expectedError, notification.firstError().message());
    }

}
