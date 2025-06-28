package com.cineflix.admin.catalog.application.category.update;

import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
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
}