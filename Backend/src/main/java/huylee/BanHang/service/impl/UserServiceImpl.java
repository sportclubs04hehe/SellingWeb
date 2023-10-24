package huylee.BanHang.service.impl;

import huylee.BanHang.components.JwtTokenUtils;
import huylee.BanHang.dtos.UserDTO;
import huylee.BanHang.entity.Role;
import huylee.BanHang.entity.User;
import huylee.BanHang.exception.AppException;
import huylee.BanHang.repository.RoleRepository;
import huylee.BanHang.repository.UserRepository;
import huylee.BanHang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager manager;

    @Override
    public User createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();

        if(repository.existsByPhoneNumber(phoneNumber)){ // kiểm tra số điện thoại đã tồn tại hay chưa
            throw new AppException(HttpStatus.BAD_REQUEST, "Phone Number already exists");
        }

        if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
            throw new AppException(BAD_REQUEST,"Password does not match");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new AppException(NOT_FOUND,"Role Not Found"));

        // convert from userDTO => user
        User newUSer = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();


        newUSer.setRole(role);

        // kiểm tra nếu có accountId không y/c password
        if(userDTO.getGoogleAccountId() == 0 && userDTO.getFacebookAccountId() == 0){
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUSer.setPassword(encodedPassword);
        }
        return repository.save(newUSer);
    }

    @Override
    public String login(String phoneNumber, String password) {
         User user = repository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(NOT_FOUND,"Invalid Phone Number / Password."));
        if (user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(password,user.getPassword())){
                throw new BadCredentialsException("Wrong phone number of password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, user.getAuthorities()
        );
        manager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(user);
    }
}
