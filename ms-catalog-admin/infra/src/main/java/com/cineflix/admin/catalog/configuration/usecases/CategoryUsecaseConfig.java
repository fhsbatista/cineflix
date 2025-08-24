package com.cineflix.admin.catalog.configuration.usecases;

import com.cineflix.admin.catalog.application.category.create.CreateCategoryUsecase;
import com.cineflix.admin.catalog.application.category.create.DefaultCreateCategoryUsecase;
import com.cineflix.admin.catalog.application.category.delete.DefaultDeleteCategoryUsecase;
import com.cineflix.admin.catalog.application.category.delete.DeleteCategoryUsecase;
import com.cineflix.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUsecase;
import com.cineflix.admin.catalog.application.category.retrieve.get.GetCategoryByIdUsecase;
import com.cineflix.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUsecase;
import com.cineflix.admin.catalog.application.category.retrieve.list.ListCategoriesUsecase;
import com.cineflix.admin.catalog.application.category.update.DefaultUpdateCategoryUsecase;
import com.cineflix.admin.catalog.application.category.update.UpdateCategoryUsecase;
import com.cineflix.admin.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUsecaseConfig {
    private final CategoryGateway categoryGateway;


    public CategoryUsecaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUsecase createCategoryUsecase() {
        return new DefaultCreateCategoryUsecase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUsecase updateCategoryUsecase() {
        return new DefaultUpdateCategoryUsecase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUsecase getCategoryByIdUsecase() {
        return new DefaultGetCategoryByIdUsecase(categoryGateway);
    }

    @Bean
    public ListCategoriesUsecase listCategoriesUsecase() {
        return new DefaultListCategoriesUsecase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUsecase listCategoryUsecase() {
        return new DefaultDeleteCategoryUsecase(categoryGateway);
    }
}
