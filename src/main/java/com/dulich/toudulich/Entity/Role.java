package com.dulich.toudulich.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name",nullable = false)
    String name;

    @Column(name = "description",length = 255)
    String description;

    public static String ADMIN = "ADMIN" ;
    public static String USER = "USER" ;
}
