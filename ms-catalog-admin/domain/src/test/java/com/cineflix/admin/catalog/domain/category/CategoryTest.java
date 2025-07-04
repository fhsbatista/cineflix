package com.cineflix.admin.catalog.domain.category;

import com.cineflix.admin.catalog.domain.exceptions.DomainException;
import com.cineflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {
    @Test
    void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "'name' should not be null",
                actualException.getErrors().get(0).message()
        );
    }

    @Test
    void givenAnBlankName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = " ";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "'name' should not be blank",
                actualException.getErrors().get(0).message()
        );
    }

    @Test
    void givenAnEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "'name' should not be empty",
                actualException.getErrors().get(0).message()
        );
    }

    @Test
    void givenAnNameWithLess3Chars_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Fe";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "'name' must be between 3 and 255 chars",
                actualException.getErrors().get(0).message()
        );
    }

    @Test
    void givenAnNameWithMore255Chars_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = """
                Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book book book.
                """;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "'name' must be between 3 and 255 chars",
                actualException.getErrors().get(0).message()
        );
    }

    @Test
    void givenValidEmptyDescription_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenValidFalseIsActive_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "A good movie";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        final var expectedName = "Movies";
        final var expectedDescription = "A good movie";
        final var expectedIsActive = true;

        final var category = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();
        final var actualCategory = category.deactivate();

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertFalse(category.isActive());
        Assertions.assertEquals(category.getName(), actualCategory.getName());
        Assertions.assertEquals(category.getDescription(), actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertEquals(actualCategory.getCreatedAt(), createdAt);
    }

    @Test
    public void givenValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final var expectedName = "Movies";
        final var expectedDescription = "A good movie";
        final var expectedIsActive = false;

        final var category = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertFalse(category.isActive());

        final var actualCategory = category.activate();

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertEquals(category.getName(), actualCategory.getName());
        Assertions.assertEquals(category.getDescription(), actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertEquals(actualCategory.getCreatedAt(), createdAt);
    }

    @Test
    public void givenValidCategory_whenCallUpdate_thenReturnUpdatedCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "A good movie";
        final var expectedIsActive = true;

        final var category = Category.newCategory(
                "A movie",
                "A description",
                false
        );

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var updatedCategory = category.update(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(category.getId(), updatedCategory.getId());
        Assertions.assertTrue(updatedCategory.isActive());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(updatedCategory.getCreatedAt(), createdAt);
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(updatedCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdateToInactive_thenReturnUpdatedCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "A good movie";
        final var expectedIsActive = false;

        final var category = Category.newCategory(
                "A movie",
                "A description",
                true
        );

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var updatedCategory = category.update(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(category.getId(), updatedCategory.getId());
        Assertions.assertFalse(updatedCategory.isActive());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(updatedCategory.getCreatedAt(), createdAt);
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(updatedCategory.getDeletedAt());
    }
}
