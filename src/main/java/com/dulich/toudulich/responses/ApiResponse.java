package com.dulich.toudulich.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;  // Trạng thái của response, thành công hoặc thất bại
    private String message;   // Thông điệp chi tiết (thường là thông báo lỗi hoặc thành công)
    private T data;           // Dữ liệu trả về (có thể là bất kỳ kiểu dữ liệu nào)
    public static <T> ApiResponse<T> withData(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    // Phương thức tiện ích trả về phản hồi thất bại
    public static <T> ApiResponse<T> withError(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
