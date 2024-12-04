package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.TourDTO;
import com.dulich.toudulich.DTO.UserDTO;
import com.dulich.toudulich.DTO.UserDTOUpdate;
import com.dulich.toudulich.DTO.UserLoginDTO;
import com.dulich.toudulich.Model.TourModel;
import com.dulich.toudulich.Model.UserModel;
import com.dulich.toudulich.Service.iUserService;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.exceptions.InvalidParamException;
import com.dulich.toudulich.responses.LoginResponse;
import com.dulich.toudulich.responses.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            UserModel userModel = userService.updateTour(id,userDTOUpdate) ;
            return ResponseEntity.ok(userModel) ;
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
