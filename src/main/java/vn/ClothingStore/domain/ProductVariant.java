package vn.ClothingStore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.util.List;
@Entity
@Table(name ="productVariants" )
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String size;   // S, M, L, 29, 30, 31
    private String color;  // Đỏ, Xanh, Trắng

    @DecimalMin(value = "0.0")
    private float price;

    private int stockQuantity; // tồn kho riêng cho biến thể

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private List<OrderDetail> orderDetails;
}
