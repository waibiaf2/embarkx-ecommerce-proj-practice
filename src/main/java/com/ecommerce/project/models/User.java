package com.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    @NotBlank
    @Size(min = 2, max = 20, message = "Username must be at least 20 characters long")
    @Column(name = "username")
    private String userName;
    
    @NotBlank
    @Email
    @Size(min= 3, max = 50, message = "Email must be between 3 and 50 characters long")
    @Column(name = "email")
    private String email;
    
    private String password;
    
    public User(
        String username,
        String email,
        String password
    ) {
        this.userName = username;
        this.email = email;
        this.password = password;
    }
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    /*@ToString.Exclude
    @OneToMany(
        mappedBy = "user",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true
    )
    private Set<Product> products;*/
    
}
