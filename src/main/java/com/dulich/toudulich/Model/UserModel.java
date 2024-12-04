package com.dulich.toudulich.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id ;

    @Column(name = "name",nullable = false,length = 255)
    private String name ;

    @Column(name = "phone",nullable = false,length = 15)
    private String phone ;

    @Column(name ="password", nullable = false, length = 255)
    private String passWord ;

    @Column(name = "gender",length = 10)
    private String gender ;

    @Column(name = "email",length = 255)
    private String email ;

    @Column(name = "address",length = 255)
    private String address ;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleModel roleId ;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>() ;
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRoleId().getRoleName().toUpperCase()));
        //authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN")) ;
        return authorityList ;
    }

    @Override
    public String getPassword() {
        return passWord;
    }

    @Override
    public String getUsername() {
        return getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
