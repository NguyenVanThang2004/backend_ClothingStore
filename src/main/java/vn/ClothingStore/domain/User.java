package vn.ClothingStore.domain;
import jakarta.persistence.*;
import org.aspectj.weaver.ast.Or;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String fullName;
    private String phoneNumber ;
    private String address ;
    private String password ;
    private Instant createdAt ;
    private Instant updatedAt ;
    private boolean isActive ;
    private LocalDate dateOfBirth ;
    private boolean facebookLinked;
    private boolean googleLinked;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "user")
    private List<SocialAccount> socialAccounts;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;


}
