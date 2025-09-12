package vn.ClothingStore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ClothingStore.domain.Category;
import vn.ClothingStore.service.CategoryService;
import vn.ClothingStore.util.annotation.ApiMessage;
import vn.ClothingStore.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    @ApiMessage("GetAll categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.getAllCategory());
    }

    @PostMapping("/categories")
    @ApiMessage("create success")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws IdInvalidException {
        boolean existsByName = this.categoryService.existsByName(category.getName());
        if (existsByName) {
            throw new IdInvalidException(
                    "category " + category.getName() + "đã tồn tại, vui lòng sử dụng email khác.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.createCategory(category));
    }

    @DeleteMapping("/categories/{id}")
    @ApiMessage("Delete category success")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") int id) throws IdInvalidException {

        Category currentCategory = this.categoryService.fetchCategoryById(id);

        if (currentCategory == null) {
            throw new IdInvalidException("Category với id = " + id + " không tồn tại");
        }
        this.categoryService.deleteCategory(id);

        return ResponseEntity.ok(null);
    }

}
