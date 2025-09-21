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
import vn.ClothingStore.domain.request.product.ReqProductDTO;
import vn.ClothingStore.domain.response.ResultPaginationDTO;
import vn.ClothingStore.domain.response.product.ResProductDTO;
import vn.ClothingStore.domain.response.product.ResUpdateProductDTO;
import vn.ClothingStore.service.CategoryService;
import vn.ClothingStore.service.ProductService;
import vn.ClothingStore.util.annotation.ApiMessage;
import vn.ClothingStore.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ApiMessage("Get products success")
    public ResponseEntity<ResultPaginationDTO> getAllProduct(
            @Filter Specification<Product> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.productService.fetchAllProduct(spec, pageable));
    }

    @PostMapping
    @ApiMessage("Create product success")
    public ResponseEntity<ResProductDTO> createProduct(
            @Valid @RequestBody ReqProductDTO req) throws IdInvalidException {
        Product product = this.productService.createProduct(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.productService.convertToResProductDTO(product));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update product success")
    public ResponseEntity<ResProductDTO> updateProduct(
            @PathVariable("id") int id,
            @Valid @RequestBody ReqProductDTO req) throws IdInvalidException {
        Product saved = this.productService.updateProduct(id, req);
        return ResponseEntity.ok(this.productService.convertToResProductDTO(saved));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete product success")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id) throws IdInvalidException {
        Product currentProduct = this.productService.fetchProductById(id);
        if (currentProduct == null) {
            throw new IdInvalidException("Product với id = " + id + " không tồn tại");
        }
        this.productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
