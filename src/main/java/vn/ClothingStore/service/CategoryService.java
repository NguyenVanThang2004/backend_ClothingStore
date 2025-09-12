package vn.ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.ClothingStore.domain.Category;
import vn.ClothingStore.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category fetchCategoryById(int id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        return null;
    }

    public boolean existsByName(String name) {
        return this.categoryRepository.existsByName(name);
    }

    public Category createCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    public void deleteCategory(int id) {
        this.categoryRepository.deleteById(id);
    }

}
