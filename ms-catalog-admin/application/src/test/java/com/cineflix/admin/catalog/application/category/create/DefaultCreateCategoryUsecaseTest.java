package com.cineflix.admin.catalog.application.category.create;

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

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

public class DefaultCreateCategoryUsecaseTest {
    @InjectMocks
    private DefaultCreateCategoryUsecase usecase;

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
    public void givenValidCommand_whenCallExecute_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Horror";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = usecase.execute(command).get();

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

        Mockito.when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var notification = usecase.execute(command).getLeft();

        Mockito.verify(gateway, Mockito.times(0)).create(any());
        Assertions.assertEquals(1, notification.getErrors().size());
        Assertions.assertEquals(
                "'name' should not be null",
                notification.firstError().message()
        );

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

        Mockito.when(gateway.create(any())).thenThrow(new IllegalStateException(expectedError));

        final var notification = usecase.execute(command).getLeft();

        Assertions.assertEquals(1, notification.getErrors().size());
        Assertions.assertEquals(expectedError, notification.firstError().message());
        Mockito.verify(gateway, Mockito.times(1)).create(argThat(category ->
                Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())));

    }
}