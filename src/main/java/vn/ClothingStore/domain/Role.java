package vn.ClothingStore.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String name ;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
