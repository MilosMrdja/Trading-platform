package com.trade_app.service.interfaces;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.UserRequest;
import com.trade_app.dto.response.UserResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;

public interface UserService {

    PageResponse<UserResponse> getAll(Pageable pageable);

    UserResponse createBroker(UserRequest userRequest) throws BadRequestException;

    UserResponse createManager(UserRequest userRequest) throws BadRequestException;
    UserResponse updateUser(Long id, UserRequest userRequest) throws BadRequestException;
}
