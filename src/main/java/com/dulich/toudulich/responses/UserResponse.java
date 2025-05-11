package com.dulich.toudulich.responses;
import com.dulich.toudulich.Entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .email(user.getEmail())
                .gender(String.valueOf(user.getGender()))
                .roleId(user.getRoleId())
                .build();
    }
}
