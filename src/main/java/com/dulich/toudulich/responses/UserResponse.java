package com.dulich.toudulich.responses;
import com.dulich.toudulich.Model.UserModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone ;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    @JsonProperty("gender")
    private String gender;

    private String roleId ;
    public static UserResponse fromUser(UserModel userModel) {
        return UserResponse.builder()
                .id(userModel.getId())
                .name(userModel.getName())
                .phone(userModel.getPhone())
                .address(userModel.getAddress())
                .email(userModel.getEmail())
                .gender(userModel.getGender())
                .roleId(userModel.getRoleId().getRoleName())
                .build();
    }
}
