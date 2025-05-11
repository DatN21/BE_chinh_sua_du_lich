package com.dulich.toudulich.Entity;

import com.dulich.toudulich.enums.Gender;
import com.dulich.toudulich.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id ;

    @Column(name = "password",nullable = false,length = 255)
    private String password ;

    @Column(name ="email")
    private String email ;

    @Column(name = "phone",length = 20,nullable = false)
    private String phone ;

    @Column(name = "status",length = 50)
    UserStatus  status ;

    @Column(name = "created_at")
    private LocalDateTime createdAt ;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt ;

    @Column(name = "name")
    private String name ;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "address")
    private String address ;
    // Quan hệ N-N thông qua bảng user_roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

  public String getRoleId() {
      return roles.stream()
                  .map(role -> String.valueOf(role.getId()))
                  .findFirst()
                  .orElse(null);
  }
}
