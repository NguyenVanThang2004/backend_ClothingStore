package vn.ClothingStore.dtos;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    @NotBlank(message = "Tên category không được để trống")
    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
