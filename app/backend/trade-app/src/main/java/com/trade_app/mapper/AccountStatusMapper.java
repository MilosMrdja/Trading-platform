package com.trade_app.mapper;

import com.trade_app.domain.entity.AccountStatus;
import com.trade_app.dto.request.AccountStatusRequest;
import com.trade_app.dto.response.AccountStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountStatusMapper {

    //@Mapping(source = "ote", target = "ote")
    AccountStatusResponse toResponse(AccountStatus accountStatus);

    AccountStatus toEntity(AccountStatusRequest accountStatusRequest);
}
