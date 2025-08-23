package vn.ClothingStore.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ClothingStore.domain.Category;
import vn.ClothingStore.dtos.CategoryDTO;
import vn.ClothingStore.service.CategoryService;

import java.util.Map;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService ;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllCategory() {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.GetAllCategory());
    }


    @PostMapping
    // neu tham so truyen vao la 1 object thi dung data transfer object = request object
    public ResponseEntity<Category> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.InsertCategory(categoryDTO));

    }
    @PutMapping
    public ResponseEntity<String> updateCategory(){
        return ResponseEntity.ok("update category");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") int id ){
        return ResponseEntity.ok("delete category "+id );
    }



}
