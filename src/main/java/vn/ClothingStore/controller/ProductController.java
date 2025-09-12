package vn.ClothingStore.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.ClothingStore.domain.Category;
import vn.ClothingStore.domain.Product;
import vn.ClothingStore.domain.response.ResultPaginationDTO;
import vn.ClothingStore.domain.response.product.ResProductDTO;
import vn.ClothingStore.domain.response.product.ResUpdateProductDTO;
import vn.ClothingStore.service.CategoryService;
import vn.ClothingStore.service.ProductService;
import vn.ClothingStore.util.annotation.ApiMessage;
import vn.ClothingStore.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;

    }

    @GetMapping("/products")
    @ApiMessage("get products success")
    public ResponseEntity<ResultPaginationDTO> getAllProduct(
            @Filter Specification<Product> spec,
            Pageable pageable) {

        return ResponseEntity.ok(this.productService.fetchAllProduct(spec, pageable));
    }

    @PostMapping("/products")
    @ApiMessage("create products success")
    public ResponseEntity<ResProductDTO> createProduct(@Valid @RequestBody Product product) throws IdInvalidException {

        // check category id
        int categoryID = product.getCategory().getId();
        Category category = categoryService.fetchCategoryById(categoryID);
        if (category == null) {
            throw new IdInvalidException("error: categoryID not in database");
        }
        product.setCategory(category);
        Product product1 = this.productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.convertToResProductDTO(product1));
    }

    @PutMapping("/products/{id}")
    @ApiMessage("update products success")
    public ResponseEntity<ResUpdateProductDTO> updateProduct(
            @PathVariable("id") int id,
            @Valid @RequestBody Product product) throws IdInvalidException {

        Product currentProduct = this.productService.fetchProductById(id);
        if (currentProduct == null) {
            throw new IdInvalidException("Product với id = " + id + " không tồn tại");
        }

        // check category
        if (product.getCategory() != null && product.getCategory().getId() > 0) {
            Category category = categoryService.fetchCategoryById(product.getCategory().getId());
            if (category == null) {
                throw new IdInvalidException("error: categoryID not in database");
            }
            currentProduct.setCategory(category);
        }

        Product saved = this.productService.updateProduct(id, product);
        return ResponseEntity.ok(this.productService.convertToResUpdateProductDTO(saved));
    }

    @DeleteMapping("products/{id}")
    @ApiMessage("delete product success")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id) throws IdInvalidException {

        Product currentProduct = this.productService.fetchProductById(id);

        if (currentProduct == null) {
            throw new IdInvalidException("Product voi id =" + id + " không tồn tại");
        }

        this.productService.deleteProduct(id);
        return ResponseEntity.ok(null);

    }

}
