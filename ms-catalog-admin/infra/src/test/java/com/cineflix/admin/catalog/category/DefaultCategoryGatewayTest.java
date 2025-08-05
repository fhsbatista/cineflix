package com.cineflix.admin.catalog.category;

import com.cineflix.admin.catalog.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@GatewayTest
public class DefaultCategoryGatewayTest {
    @Autowired
    private DefaultCategoryGateway gateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    public void testInjectedDependencies() {
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(repository);
    }
}
