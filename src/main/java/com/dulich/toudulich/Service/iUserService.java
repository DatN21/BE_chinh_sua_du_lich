package com.dulich.toudulich.Service;

import com.dulich.toudulich.DTO.UserDTO;
import com.dulich.toudulich.DTO.UserDTOUpdate;
import com.dulich.toudulich.DTO.UserDTOUpdateByAdmin;
import com.dulich.toudulich.Model.UserModel;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.exceptions.InvalidParamException;
import com.dulich.toudulich.exceptions.PermissionDenyException;
import com.dulich.toudulich.exceptions.UnauthorizedException;
import com.dulich.toudulich.responses.LoginResponse;
import com.dulich.toudulich.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public interface iUserService {
    UserModel createUser(UserDTO userDTO) throws PermissionDenyException;

    LoginResponse login(String phoneNumber , String password) throws DataNotFoundException, InvalidParamException;

    UserModel getUserDetailFromToken(String token) throws Exception ;


    UserModel updateUserByAdmin(int id, UserDTOUpdateByAdmin userDTOUpdate) throws DataNotFoundException, UnauthorizedException;

    UserModel getUserById(int id);

    Page<UserResponse> getUserResponse(PageRequest pageRequest);

    void deleteUser(int id) ;

    UserModel updateUser(int id, UserDTOUpdate userDTOUpdate);
}
