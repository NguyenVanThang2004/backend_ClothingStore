package vn.ClothingStore.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.ClothingStore.domain.Category;
import vn.ClothingStore.domain.Product;
import vn.ClothingStore.domain.User;
import vn.ClothingStore.domain.Product;
import vn.ClothingStore.domain.response.ResultPaginationDTO;
import vn.ClothingStore.domain.response.product.ResProductDTO;
import vn.ClothingStore.domain.response.product.ResUpdateProductDTO;
import vn.ClothingStore.repository.ProductRepository;
import vn.ClothingStore.util.error.IdInvalidException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public ResProductDTO convertToResProductDTO(Product product) {
        ResProductDTO res = new ResProductDTO();
        res.setId(product.getId());
        res.setName(product.getName());
        res.setPrice(product.getPrice());
        res.setDescription(product.getDescription());

        if (product.getCategory() != null) {
            res.setCategory(
                    new ResProductDTO.CategoryDTO(product.getCategory().getId(), product.getCategory().getName()));
        }

        return res;
    }

    // get all product
    public ResultPaginationDTO fetchAllProduct(Specification<Product> spec, Pageable pageable) {

        Page<Product> pageProduct = this.productRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageProduct.getTotalPages());
        mt.setTotal(pageProduct.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResProductDTO> listProduct = pageProduct.getContent()
                .stream().map(item -> this.convertToResProductDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listProduct);

        return rs;
    }

    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }

    public Product updateProduct(int id, Product product) {

        Product currentProduct = this.fetchProductById(id);

        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setDescription(product.getDescription());
        currentProduct.setCategory(product.getCategory());

        currentProduct = this.productRepository.save(currentProduct);

        return currentProduct;
    }

    public ResUpdateProductDTO convertToResUpdateProductDTO(Product product) {
        ResUpdateProductDTO res = new ResUpdateProductDTO();
        res.setId(product.getId());
        res.setName(product.getName());
        res.setPrice(product.getPrice());
        res.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            res.setCategory(
                    new ResUpdateProductDTO.CategoryDTO(product.getCategory().getId(),
                            product.getCategory().getName()));
        }
        return res;
    }

    public Product fetchProductById(int id) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        return null;
    }

    public void deleteProduct(int id) {
        this.productRepository.deleteById(id);
    }

}
