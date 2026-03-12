package com.example.taskmanagement.service;

import com.example.taskmanagement.Util.JwtUtil;
import com.example.taskmanagement.dto.ApiResponse;
import com.example.taskmanagement.dto.request.LoginRequest;
import com.example.taskmanagement.dto.request.RegisterRequest;
import com.example.taskmanagement.dto.response.LoginResponse;
import com.example.taskmanagement.dto.response.RegisterResponse;
import com.example.taskmanagement.dto.response.UserResponse;
import com.example.taskmanagement.entity.RoleEntity;
import com.example.taskmanagement.entity.UserEntity;
import com.example.taskmanagement.exception.ConflictException;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.repository.RoleRepository;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public RegisterResponse regiseter(RegisterRequest registerRequest){
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            throw new ConflictException("Email already exists");
        }
        String password=passwordEncoder.encode(registerRequest.getPassword());
        UserEntity userEntity =new UserEntity(
                registerRequest.getName(),
                registerRequest.getEmail(),
                password);
        RoleEntity role=roleRepository.findByRoleName("USER").orElseThrow(
                () -> new ResourceNotFoundException("Role not found")
        );
        userEntity.getRoles().add(role);
        userRepository.save(userEntity);
        return new RegisterResponse(userEntity);
    }

    public LoginResponse login(LoginRequest loginRequest){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        String accessToken= jwtUtil.generateToken(userDetails);
        return new LoginResponse(accessToken);
    }
}
