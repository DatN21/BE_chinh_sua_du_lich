package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.*;
import com.dulich.toudulich.Entity.User;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Service.iUser;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.exceptions.InvalidParamException;
import com.dulich.toudulich.exceptions.UnauthorizedException;
import com.dulich.toudulich.responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final iUser userService ;
    @GetMapping("")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/register")
    public ApiResponse<UserDTO> register( @RequestBody UserDTO userDTO){
        try {
                return userService.createUser(userDTO);
        } catch (Exception var4) {
            return ApiResponse.withError(var4.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO)
            throws DataNotFoundException, InvalidParamException {
            return userService.login(userLoginDTO.getPhone(), userLoginDTO.getPassword());
    }

    @PostMapping("/details")
    public ApiResponse<User> getUserDetail(@RequestHeader("Authorization") String token) {
        try {
            String extracked = token.substring(7) ;
            return userService.getUserDetailFromToken(extracked) ;
        }catch (Exception e)
        {
            return ApiResponse.withError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateUser( @PathVariable int id,
                                         @Valid @RequestBody UserDTOUpdate userDTOUpdate){
        try {
            return userService.updateUser(id,userDTOUpdate) ;
        }catch (Exception  e){
            return ApiResponse.withError(e.getMessage());
        }
    }

    @GetMapping("/full")
    public ApiResponse<Page<UserResponse>> getAllUser(
            Pageable pageable
    ){
        return userService.getUserResponse(pageable) ;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, String>> deleteUser(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            // Gọi service để xoá người dùng
            userService.deleteUser(id);
            response.put("message", "Người dùng đã được xóa thành công!");
            return ApiResponse.withData(response, "Người dùng đã được xóa thành công!");
        } catch (Exception e) {
            response.put("message", "Không thể xóa người dùng. Vui lòng thử lại!");
            return ApiResponse.withError(response.get("message"));
        }
    }

    @PutMapping("/admin/{id}")
    public ApiResponse<?> updateUserByAdmin( @PathVariable int id,
                                         @Valid @RequestBody UserDTOUpdateByAdmin userDTOUpdate){
        try {
            return userService.updateUserByAdmin(id,userDTOUpdate) ;
        }catch (Exception | UnauthorizedException e){
            return ApiResponse.withError(e.getMessage());
        }
    }
}
