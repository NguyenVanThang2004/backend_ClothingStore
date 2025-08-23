package vn.ClothingStore.service;

import org.springframework.stereotype.Service;
import vn.ClothingStore.domain.Category;
import vn.ClothingStore.dtos.CategoryDTO;
import vn.ClothingStore.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService{
    private final CategoryRepository categoryRepository ;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> GetAllCategory(){
        return this.categoryRepository.findAll() ;
    }
    public Category InsertCategory(CategoryDTO categoryDTO){

        Category category = new Category();
        category.setName(categoryDTO.getName());

        return this.categoryRepository.save(category);
    }
}
