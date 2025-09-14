package vn.ClothingStore.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUploadProductVariantDTO {
    private float price;
    private int stockQuantity;
}
