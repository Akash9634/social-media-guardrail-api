package com.media.socialgaurd.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column = maps to a database column
    // unique = no two users can have same username
    // nullable = username cannot be empty
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // we'll store hashed password, never plain text!

    @Column(nullable = false)
    private String email;

    // is_premium flag - regular users vs premium users
    @Column(name = "is_premium")
    private Boolean isPremium = false; // default is false
}
