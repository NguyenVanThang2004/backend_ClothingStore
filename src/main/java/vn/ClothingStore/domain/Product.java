package vn.ClothingStore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String name ;
    // ten san pham
    @DecimalMin(value = "0.0")
    private float price ;

    private String thumbnail ;//url , duong dan anh dai dien

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Instant createdAt ;
    private Instant updatedAt ;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> variants; // danh sách biến thể

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;



}
