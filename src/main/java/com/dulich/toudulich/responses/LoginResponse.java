package com.dulich.toudulich.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {

        private String message; // Thông báo về trạng thái đăng nhập
        private String token; // Tên người dùng



    // Getters và Setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
