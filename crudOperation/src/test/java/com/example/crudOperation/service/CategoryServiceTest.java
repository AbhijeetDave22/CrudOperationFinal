package com.example.crudOperation.service;

import com.example.crudOperation.exception.ResourceNotFoundException;
import com.example.crudOperation.pojos.Category;
import com.example.crudOperation.repository.CategoryRepository;
import com.example.crudOperation.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        category = new Category("Electronics", "Electronic gadgets and devices");
        category.setId(1L);
    }

    @Test
    void shouldSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(category);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void shouldGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        categoryService.getAllCategories();

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void shouldGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.getCategoryById(1L);

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.updateCategory(1L, category);

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void shouldDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        verify(categoryRepository, never()).deleteById(1L);
    }
}


