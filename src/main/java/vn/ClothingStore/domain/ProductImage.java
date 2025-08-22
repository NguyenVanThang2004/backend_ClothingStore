package vn.ClothingStore.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
