package com.cineflix.admin.catalog.application.category.update;

import com.cineflix.admin.catalog.application.category.create.CreateCategoryCommand;
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

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

class DefaultUpdateCategoryUsecaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUsecase usecase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidCommand_whenCallExecute_shouldReturnCategoryId() {
        final var category = Category.newCategory("Theater", null, true);
        final var expectedId = category.getId();
        final var expectedName = "Movies";
        final var expectedDescription = "Humor";
        final var expectedIsActive = true;


        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito
                .when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category));

        Mockito
                .when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var output = usecase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(updatedCategory -> {
            return Objects.equals(expectedId.getValue(), updatedCategory.getId().getValue())
                    && Objects.equals(expectedName, updatedCategory.getName())
                    && Objects.equals(expectedDescription, updatedCategory.getDescription())
                    && Objects.equals(expectedIsActive, updatedCategory.isActive())
                    && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                    && Objects.isNull(updatedCategory.getDeletedAt());
        }));
    }

    @Test
    public void givenInvalidName_whenCallExecute_shouldReturnDomainException() {
        final Category category = Category.newCategory("Film", null, true);
        final String expectedId = "123";
        final String expectedName = null;
        final var expectedDescription = "Horror";
        final var expectedIsActive = true;

        final var command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(any())).thenReturn(Optional.of(category));

        final var notification = usecase.execute(command).getLeft();

        Mockito.verify(categoryGateway, times(0)).update(any());
        Assertions.assertEquals(1, notification.getErrors().size());
        Assertions.assertEquals(
                "'name' should not be null",
                notification.firstError().message()
        );

    }

    @Test
    public void givenValidInactivateCommand_whenCallExecute_shouldReturnInactivCategoryId() {
        final var category = Category.newCategory("Theater", null, true);
        final var expectedId = category.getId();
        final var expectedName = "Movies";
        final var expectedDescription = "Humor";
        final var expectedIsActive = false;


        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito
                .when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category));

        Mockito
                .when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var output = usecase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(updatedCategory -> {
            return Objects.equals(expectedId.getValue(), updatedCategory.getId().getValue())
                    && Objects.equals(expectedName, updatedCategory.getName())
                    && Objects.equals(expectedDescription, updatedCategory.getDescription())
                    && Objects.equals(expectedIsActive, updatedCategory.isActive())
                    && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                    && Objects.isNull(updatedCategory.getDeletedAt());
        }));
    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final var category = Category.newCategory("Theater", null, true);
        final var expectedId = category.getId();
        final var expectedName = "Movie";
        final var expectedDescription = "Horror";
        final var expectedIsActive = true;
        final var expectedError = "Gateway error";

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category));
        Mockito.when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedError));

        final var notification = usecase.execute(command).getLeft();

        Assertions.assertEquals(1, notification.getErrors().size());
        Assertions.assertEquals(expectedError, notification.firstError().message());
        Mockito.verify(categoryGateway, times(1)).update(argThat(updatedCategory -> {
            return Objects.equals(expectedId.getValue(), updatedCategory.getId().getValue())
                    && Objects.equals(expectedName, updatedCategory.getName())
                    && Objects.equals(expectedDescription, updatedCategory.getDescription())
                    && Objects.equals(expectedIsActive, updatedCategory.isActive())
                    && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                    && Objects.isNull(updatedCategory.getDeletedAt());
        }));
    }

    @Test
    public void givenNotExistentId_whenCallExecute_shouldReturnNotFoundException() {
        final String expectedId = "123";
        final String expectedName = null;
        final var expectedDescription = "Horror";
        final var expectedIsActive = true;

        final var command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(
                DomainException.class,
                () -> usecase.execute(command).getLeft()
        );

        Assertions.assertEquals(
                "Category with id %s was not found".formatted(expectedId),
                exception.getErrors().getFirst().message()
        );

        Assertions.assertEquals(1, exception.getErrors().size());
        Mockito.verify(categoryGateway, times(0)).update(any());
    }
}