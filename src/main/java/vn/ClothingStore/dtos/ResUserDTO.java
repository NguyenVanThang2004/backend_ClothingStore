package vn.ClothingStore.dtos;

import java.time.Instant;

import javax.management.relation.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ResUserDTO {

    private int id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Instant createdAt;
    private Instant updateAt;

}
