package com.trade_app.mapper;

import com.trade_app.domain.entity.Account;
import com.trade_app.dto.request.AccountRequest;
import com.trade_app.dto.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    //@Mapping(target = "name", source = "name")
    AccountResponse toResponse(Account account);

    //@Mapping(target = "name", source = "name")
    Account toEntity(AccountRequest accountRequest);
}
