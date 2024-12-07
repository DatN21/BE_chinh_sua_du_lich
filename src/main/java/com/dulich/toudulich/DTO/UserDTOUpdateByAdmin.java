package com.dulich.toudulich.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTOUpdateByAdmin {
    @NotBlank(message = "Họ và tên không được bỏ trống")
    private String name ;


    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String phone ;

    @NotBlank(message = "Giới tính không được bỏ trống")
    private String gender ;

    @NotBlank(message = "Email không được bỏ trống")
    private String email ;

    @NotBlank(message = "Địa chỉ không được bỏ trống")
    private String address ;

    private Long roleId ;
}
