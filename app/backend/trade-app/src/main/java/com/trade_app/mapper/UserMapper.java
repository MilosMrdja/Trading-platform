package com.trade_app.mapper;

import com.trade_app.domain.entity.User;
import com.trade_app.dto.request.UserRequest;
import com.trade_app.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //@Mapping(target = "name", source = "name")
    UserResponse toResponse(User user);

    User toEntity(UserRequest userRequest);
}
