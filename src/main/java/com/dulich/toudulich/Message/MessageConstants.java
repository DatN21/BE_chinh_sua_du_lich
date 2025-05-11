package com.dulich.toudulich.Message;

public class MessageConstants {
    // Thông báo thành công
    public static final String SUCCESS = "Thành công";
    public static final String CREATED_SUCCESSFULLY = "Tạo mới thành công";
    public static final String UPDATED_SUCCESSFULLY = "Cập nhật thành công";
    public static final String DELETED_SUCCESSFULLY = "Xóa thành công";

    // Thông báo lỗi chung
    public static final String ERROR_OCCURRED = "Đã xảy ra lỗi";
    public static final String NOT_FOUND = "Không tìm thấy dữ liệu";
    public static final String INVALID_INPUT = "Dữ liệu đầu vào không hợp lệ";
    public static final String UNAUTHORIZED = "Không có quyền truy cập";
    public static final String STATUS_NOT_FOUND = "Trạng thái không tồn tại";

    // Thông báo người dùng
    public static final String USER_EXISTS = "Số điện thoại đã tồn tại";
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng";
    public static final String CANNOT_CREATE_ADMIN = "Bạn không có quyền tạo tài khoản admin";
    public static final String USERNAME_OR_PASSWORD_INVALID = "Tên đăng nhập hoặc mật khẩu không hợp lệ";
    public static final String USERNAME_OR_PASSWORD_EMPTY = "Tên đăng nhập hoặc mật khẩu không được để trống";
    public static final String USERNAME_OR_PASSWORD_NOT_FOUND = "Tên đăng nhập hoặc mật khẩu không tồn tại";
    public static final String USERNAME_OR_PASSWORD_INCORRECT = "Tên đăng nhập hoặc mật khẩu không chính xác";
    public static final String USERNAME_OR_PASSWORD_NOT_MATCH = "Tên đăng nhập hoặc mật khẩu không khớp";
    public static final String USER_NOT_FOUND_BY_ID = "Người dùng không tồn tại";


    // Thông báo vai trò
    public static final String ROLE_NOT_FOUND = "Phân quyền không tồn tại";

    // Thông báo booking
    public static final String BOOKING_SUCCESS = "Đặt tour thành công";
    public static final String BOOKING_NOT_FOUND = "Không tìm thấy thông tin đặt tour";

    // Thông báo ảnh
    public static final String IMAGE_NOT_FOUND = "Không tìm thấy ảnh";
    public static final String IMAGE_UPLOAD_SUCCESS = "Tải ảnh lên thành công";
    public static final String IMAGE_DELETE_SUCCESS = "Xóa ảnh thành công";
    public static final String IMAGE_UPLOAD_FAILED = "Tải ảnh lên thất bại";
    public static final String IMAGE_FORMAT_INVALID = "Định dạng ảnh không hợp lệ";
    public static final String IMAGE_SIZE_EXCEEDED = "Kích thước ảnh vượt quá giới hạn cho phép";
    public static final String IMAGE_NOT_FOUND_BY_TOUR = "Không tìm thấy ảnh theo tourId";
    public static final String GET_IMAGE_SUCCESS = "Lấy danh sách ảnh thành công";

    // Thông báo tour
    public static final String TOUR_NOT_FOUND = "Không tìm thấy tour";
    public static final String TOUR_EXISTS = "Tour đã tồn tại";
    public static final String IMAGE_UPLOAD_LIMIT = "Giới hạn tải lên ảnh là 5 ảnh cho mỗi tour";

    private MessageConstants() {
        // Không cho phép khởi tạo class này
    }
}
