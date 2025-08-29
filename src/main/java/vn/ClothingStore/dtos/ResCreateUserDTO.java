package vn.ClothingStore.dtos;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {

    private int id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Instant createdAt;

}
