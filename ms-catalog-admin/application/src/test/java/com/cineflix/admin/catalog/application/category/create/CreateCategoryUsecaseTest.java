package com.cineflix.admin.catalog.application.category.create;

import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

class CreateCategoryUsecaseTest {
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

        final var gateway = Mockito.mock(CategoryGateway.class);
        Mockito.when(gateway.create(Mockito.any())).thenAnswer(returnsFirstArg());
        final var usecase = new DefaultCreateCategoryUsecase(gateway);

        final var output = usecase.execute(command);

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1)).create(
                Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName()) &&
                                Objects.equals(expectedDescription, category.getDescription()) &&
                                Objects.equals(expectedIsActive, category.isActive()) &&
                                Objects.nonNull(category.getId()) &&
                                Objects.nonNull(category.getCreatedAt()) &&
                                Objects.nonNull(category.getUpdatedAt()) &&
                                Objects.isNull(category.getDeletedAt())

                )
        );
    }
}