package com.cineflix.admin.catalog.application.category.delete;

import com.cineflix.admin.catalog.IntegrationTest;
import com.cineflix.admin.catalog.domain.category.Category;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import com.cineflix.admin.catalog.domain.category.CategoryId;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryJpaEntity;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteCategoryUsecaseTestIt {
    @Autowired
    private DeleteCategoryUsecase usecase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidId_WhenCallsExecute_shouldBeOk() {
        final var category = Category.newCategory("Films", "Most watched", true);
        repository.saveAndFlush(CategoryJpaEntity.from(category));

        Assertions.assertEquals(1, repository.count());

        Assertions.assertDoesNotThrow(() -> usecase.execute(category.getId().getValue()));

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAInvalidId_WhenCallsExecute_shouldBeOk() {
        final var id = CategoryId.from("123");

        Assertions.assertEquals(0, repository.count());

        Assertions.assertDoesNotThrow(() -> usecase.execute(id.getValue()));

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAValidId_WhenGatewayThrowsError_shouldReturnException() {
        final var id = CategoryId.from("123");
        Mockito.doThrow(new IllegalStateException()).when(gateway).deleteById(Mockito.eq(id));

        Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(id.getValue()))  ;

        Mockito.verify(gateway, Mockito.atMostOnce()).deleteById(id);
    }
}
