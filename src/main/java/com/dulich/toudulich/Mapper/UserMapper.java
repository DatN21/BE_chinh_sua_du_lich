package com.dulich.toudulich.Mapper;

import com.dulich.toudulich.DTO.UserDTO;
import com.dulich.toudulich.Entity.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .gender(user.getGender().toString())
                .address(user.getAddress())
                .build();
    }
}
