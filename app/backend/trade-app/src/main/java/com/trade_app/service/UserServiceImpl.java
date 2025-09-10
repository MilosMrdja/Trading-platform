package com.trade_app.service;

import com.trade_app.common.exception.BadRequestError;
import com.trade_app.common.exception.NotFoundError;
import com.trade_app.common.shared.PageResponse;
import com.trade_app.domain.entity.User;
import com.trade_app.dto.request.RegisterUserDTO;
import com.trade_app.dto.request.UserRequest;
import com.trade_app.dto.response.UserResponse;
import com.trade_app.mapper.UserMapper;
import com.trade_app.repository.UserRepository;
import com.trade_app.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;


    @Override
    public PageResponse<UserResponse> getAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);

        List<UserResponse> content = page.getContent()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Transactional
    @Override
    public UserResponse createBroker(UserRequest userRequest) throws BadRequestException {

        if(userRequest.getName().length() < 3) {
            throw new BadRequestException("Name must be at least 3 characters long");
        }
        if (userRequest.getEmail() == null || !userRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRequestError("Invalid email format");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().length() < 6) {
            throw new BadRequestError("Password must be at least 6 characters long");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new BadRequestError("User with this email already exists");
        }
        return authenticationService.signup(new RegisterUserDTO(userRequest.getName(), userRequest.getEmail(), userRequest.getPassword(), "BROKER"));
    }

    @Transactional
    @Override
    public UserResponse createManager(UserRequest userRequest) throws BadRequestException {
        if(userRequest.getName().length() < 3) {
            throw new BadRequestException("Name must be at least 3 characters long");
        }
        if (userRequest.getEmail() == null || !userRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRequestError("Invalid email format");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().length() < 6) {
            throw new BadRequestError("Password must be at least 6 characters long");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new BadRequestError("User with this email already exists");
        }
        return authenticationService.signup(new RegisterUserDTO(userRequest.getName(), userRequest.getEmail(), userRequest.getPassword(), "MANAGER"));

    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) throws BadRequestException {
        if(userRequest.getName().length() < 3) {
            throw new BadRequestException("Name must be at least 3 characters long");
        }
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundError("User not found"));
        user.setName(userRequest.getName() == null ? user.getName() : userRequest.getName());
        return userMapper.toResponse(userRepository.save(user));
    }
}
