package com.dulich.toudulich.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoles {
    @Id
    @Column(name = "user_id", nullable = false)
    private int userId ;

    @Column(name = "role_id", nullable = false)
    private int roleId ;
}
