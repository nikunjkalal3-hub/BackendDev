package backend.backenddev.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.function.Predicate;

@Getter
@Entity
@Table(name="users")
public class User {


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;

    @Setter
    @Column(unique = true,nullable = false)
    private String email;

    @Setter
    @Column(unique = false)
    private String password;

    public  User(){
    }

    public User(String name,String email,String password){
        this.name=name;
        this.email=email;
        this.password=password;
        this.role=Role.USER;
    }


}
