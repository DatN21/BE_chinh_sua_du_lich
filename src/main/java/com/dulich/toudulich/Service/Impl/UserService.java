package com.dulich.toudulich.Service.Impl;

import com.dulich.toudulich.DTO.UserDTO;
import com.dulich.toudulich.DTO.UserDTOUpdate;
import com.dulich.toudulich.DTO.UserDTOUpdateByAdmin;
import com.dulich.toudulich.Entity.Role;
import com.dulich.toudulich.Entity.User;
import com.dulich.toudulich.Entity.UserRoles;
import com.dulich.toudulich.Mapper.UserMapper;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Repositories.RoleRepository;
import com.dulich.toudulich.Repositories.UserRepository;
import com.dulich.toudulich.Repositories.UserRolesRepository;
import com.dulich.toudulich.Service.iUser;
import com.dulich.toudulich.component.JwtTokenUtil;
import com.dulich.toudulich.enums.Gender;
import com.dulich.toudulich.enums.RoleType;
import com.dulich.toudulich.enums.UserStatus;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.exceptions.InvalidParamException;
import com.dulich.toudulich.exceptions.PermissionDenyException;
import com.dulich.toudulich.exceptions.UnauthorizedException;
import com.dulich.toudulich.responses.ApiResponse;
import com.dulich.toudulich.responses.LoginResponse;
import com.dulich.toudulich.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dulich.toudulich.Message.MessageConstants.CREATED_SUCCESSFULLY;
import static com.dulich.toudulich.Message.MessageConstants.DELETED_SUCCESSFULLY;
import static com.dulich.toudulich.responses.ApiResponse.withData;

@Service
@RequiredArgsConstructor
public class UserService implements iUser {
    private final UserRepository userRepository ;
    private final RoleRepository roleRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtTokenUtil jwtTokenUtil ;
    private final AuthenticationManager authenticationManager ;
    private final UserRolesRepository userRolesRepository ;

    @Override
    public ApiResponse<UserDTO> createUser(UserDTO userDTO) throws PermissionDenyException {
        String phoneNumber = userDTO.getPhone();
        if (userRepository.existsByPhone(phoneNumber)) {
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại");
        }

        String passWord = userDTO.getPassword();
        String encodePassword = passwordEncoder.encode(passWord);
        Gender gender = userDTO.getGender() != null ? Gender.valueOf(userDTO.getGender()) : Gender.OTHER;
        if (userDTO.getGender() == null) {
            gender = Gender.OTHER; // Assign a default value or handle appropriately
        }
        User newUser = User.builder()
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .password(encodePassword)
                .gender(gender)
                .name(userDTO.getName())
                .build();

        userRepository.save(newUser);
        userRolesRepository.save(UserRoles.builder()
                .userId(newUser.getId())
                .roleId(RoleType.USER.getId())
                .build());
        userDTO.setPhone(newUser.getPhone());
        userDTO.setPassword(newUser.getPassword());
        return withData(userDTO, MessageConstants.USER_REGISTER_SUCCESS);
    }

    @Override
    public ApiResponse<LoginResponse> login(String phone, String password) throws DataNotFoundException, InvalidParamException {
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();
        Optional<User> userOptional = userRepository.findByPhoneWithRoles(phone);

        if (userOptional.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number / password!");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong phone number or password");
        }

        // Lấy danh sách role để chuyển thành authorities
        Set<Role> roles = user.getRoles();

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .collect(Collectors.toList());

        // Tạo token xác thực
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(phone, password, authorities);

        authenticationManager.authenticate(authenticationToken);

        return withData(LoginResponse.builder()
                .message("Login successful")
                .token(jwtTokenUtil.generateToken(user))
                .build(), MessageConstants.LOGIN_SUCCESS);
    }


    @Override
    public ApiResponse<User> getUserDetailFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expired") ;
        }
        String phone = jwtTokenUtil.extractPhone(token) ;
        Optional<User> userModel = userRepository.findByPhoneWithRoles(phone) ;

        if (userModel.isPresent()){
            return withData(userModel.get(), MessageConstants.SUCCESS) ;
        }else {
            throw new Exception("User not found") ;
        }
    }

    @Override
    public ApiResponse<UserDTO> updateUserByAdmin(int id, UserDTOUpdateByAdmin userDTOUpdate) throws DataNotFoundException, UnauthorizedException {
        // Lấy số điện thoại người đang đăng nhập
        String currentPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByPhoneWithRoles(currentPhone)
                .orElseThrow(() -> new RuntimeException("User not found with phone: " + currentPhone));

        // Lấy người dùng cần cập nhật
        User existingUser = getUserById(id);

        // Cập nhật thông tin cơ bản
        existingUser.setPhone(userDTOUpdate.getPhone());
        existingUser.setName(userDTOUpdate.getName());
        existingUser.setEmail(userDTOUpdate.getEmail());
        existingUser.setGender(Gender.valueOf(userDTOUpdate.getGender()));
        existingUser.setAddress(userDTOUpdate.getAddress());

        // Cập nhật role nếu có và là admin
        if (userDTOUpdate.getRoleId() != null) {
            boolean isAdmin = currentUser.getRoles().stream()
                    .anyMatch(role -> role.getName().equalsIgnoreCase(RoleType.ADMIN.name()));
            if (!isAdmin) {
                throw new UnauthorizedException("Bạn không có quyền cập nhật quyền người dùng khác");
            }

            Role role = roleRepository.findById(userDTOUpdate.getRoleId())
                    .orElseThrow(() -> new DataNotFoundException("Role not found"));

            UserRoles userRoles = userRolesRepository.findByUserId(existingUser.getId());
            if (userRoles != null) {
                userRoles.setRoleId(role.getId());
                userRolesRepository.save(userRoles);
            } else {
                throw new DataNotFoundException("UserRoles not found");
            }
        }

        // Lưu cập nhật
        userRepository.save(existingUser);

        // Trả về DTO
        return withData(UserMapper.toDTO(existingUser), MessageConstants.UPDATED_SUCCESSFULLY);
    }






    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id = " + id + " not found"));
    }

    @Override
    public ApiResponse<Page<UserResponse>> getUserResponse(Pageable pageable) {
        return withData(userRepository.findAll(pageable).map(UserResponse::fromUser), MessageConstants.SUCCESS);
    }

    @Override
    public void  deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        userRepository.delete(user);
    }

    @Override
    public ApiResponse<UserDTO> updateUser(int id, UserDTOUpdate userDTOUpdate) {
        User existingUser = getUserById(id);
        existingUser.setPhone(userDTOUpdate.getPhone());
        existingUser.setName(userDTOUpdate.getName());
        existingUser.setEmail(userDTOUpdate.getEmail());
        existingUser.setGender(parseGender(userDTOUpdate.getGender()));
        existingUser.setAddress(userDTOUpdate.getAddress());
        userRepository.save(existingUser) ;
        return withData(UserMapper.toDTO(existingUser), MessageConstants.UPDATED_SUCCESSFULLY);
    }

    private Gender parseGender(String genderStr) {
        if (genderStr == null) {
            return Gender.OTHER;
        }
        String normalized = removeVietnameseTones(genderStr.trim());

        switch (normalized) {
            case "nam":
                return Gender.NAM;
            case "nu":
                return Gender.NU;
            default:
                return Gender.OTHER;
        }
    }

    public static String removeVietnameseTones(String str) {
        str = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        str = str.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return str.toLowerCase();
    }

}
