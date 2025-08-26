package com.cineflix.admin.catalog.application;

import com.cineflix.admin.catalog.IntegrationTest;
import com.cineflix.admin.catalog.application.category.create.CreateCategoryUsecase;
import com.cineflix.admin.catalog.infra.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIt {
    @Autowired
    private CreateCategoryUsecase categoryUsecase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjects() {
        Assertions.assertNotNull(categoryUsecase);
        Assertions.assertNotNull(categoryRepository);
    }
}
