package vn.ClothingStore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order ;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant; // chọn biến thể cụ thể


    @DecimalMin(value = "0.0")
    private float price ;

    @DecimalMin(value = "1")
    private  int numberOfProducts ;

    @DecimalMin(value = "0.0")
    private float totalMoney ;
}
