package com.projekt.Spring_Boot_API.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID user_id;

    @Column (nullable = false, unique = true)
    private String username;

    @Column (nullable = false)
    private String password_hash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Folder> folders;

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> items;
}
