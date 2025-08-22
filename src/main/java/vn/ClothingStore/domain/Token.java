package vn.ClothingStore.domain;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String token ; // chuoi token
    private String tokenType; // loai token
    private Instant expirationDate ; // thoi diem het han token
    private boolean revoked ; // tran thai thu hoi
    private boolean expired ; // token bi het han chua

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user ;




}
