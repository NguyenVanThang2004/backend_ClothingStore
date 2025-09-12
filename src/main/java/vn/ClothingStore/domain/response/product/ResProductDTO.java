package vn.ClothingStore.domain.response.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResProductDTO {

    private int id;
    private String name;
    private float price;
    private String description;
    private CategoryDTO category;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDTO {
        private int id;
        private String name;
    }

    public static class ProductImageDTO {
        private int id;
        private String url;
        private boolean thumbnail;
    }

}
