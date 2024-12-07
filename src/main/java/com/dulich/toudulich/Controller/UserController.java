package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.*;
import com.dulich.toudulich.Model.TourModel;
import com.dulich.toudulich.Model.UserModel;
import com.dulich.toudulich.Service.iUserService;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.exceptions.InvalidParamException;
import com.dulich.toudulich.exceptions.UnauthorizedException;
import com.dulich.toudulich.responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final iUserService userService ;
    @GetMapping("")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            } else {
                userService.createUser(userDTO);
                return ResponseEntity.ok(userDTO);
            }
        } catch (Exception var4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(var4.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO)
            throws DataNotFoundException, InvalidParamException {
        LoginResponse loginResponse = userService.login(userLoginDTO.getPhone(), userLoginDTO.getPassword());
        return ResponseEntity.ok(loginResponse); // Trả về LoginResponse
    }

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetail(@RequestHeader("Authorization") String token) {
        try {
            String extracked = token.substring(7) ;
            UserModel userModel = userService.getUserDetailFromToken(extracked) ;
            return ResponseEntity.ok(UserResponse.fromUser(userModel)) ;
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().build() ;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser( @PathVariable int id,
                                         @Valid @RequestBody UserDTOUpdate userDTOUpdate){
        try {
            UserModel userModel = userService.updateUser(id,userDTOUpdate) ;
            return ResponseEntity.ok(userModel) ;
        }catch (Exception  e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/full")
    public ResponseEntity<ListUserResponse> getAllUser(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page,limit);
        Page<UserResponse> userResponse = userService.getUserResponse(pageRequest);
        int totalPage = userResponse.getTotalPages() ;
        List<UserResponse> userResponses = userResponse.getContent();
        return ResponseEntity.ok(ListUserResponse.builder()
                .userResponses(userResponses)
                .totalPages(totalPage)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            // Gọi service để xoá người dùng
            userService.deleteUser(id);
            response.put("message", "Người dùng đã được xóa thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Không thể xóa người dùng. Vui lòng thử lại!");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateUserByAdmin( @PathVariable int id,
                                         @Valid @RequestBody UserDTOUpdateByAdmin userDTOUpdate){
        try {
            UserModel userModel = userService.updateUserByAdmin(id,userDTOUpdate) ;
            return ResponseEntity.ok(userModel) ;
        }catch (Exception | UnauthorizedException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
