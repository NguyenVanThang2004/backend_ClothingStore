package vn.ClothingStore.dtos;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
    private int id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Instant updateAt;
}
