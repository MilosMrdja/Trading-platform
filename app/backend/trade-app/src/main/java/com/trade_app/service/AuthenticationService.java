package com.trade_app.service;

import com.trade_app.common.exception.BadRequestError;
import com.trade_app.domain.entity.User;
import com.trade_app.domain.enums.Role;
import com.trade_app.dto.request.LoginUserDTO;
import com.trade_app.dto.request.RegisterUserDTO;
import com.trade_app.dto.response.UserResponse;
import com.trade_app.mapper.UserMapper;
import com.trade_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;



    public UserResponse signup(RegisterUserDTO input) {
        if (input.getEmail() == null || !input.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRequestError("Invalid email format");
        }

        if (input.getPassword() == null || input.getPassword().length() < 6) {
            throw new BadRequestError("Password must be at least 6 characters long");
        }
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new BadRequestError("User with this email already exists");
        }
        User user = new User();
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(Role.valueOf(input.getRole().toUpperCase()));
        return userMapper.toResponse(userRepository.save(user));
    }

    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}