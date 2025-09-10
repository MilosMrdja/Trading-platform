package com.trade_app.service.interfaces;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.AccountRequest;
import com.trade_app.dto.response.AccountResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    PageResponse<AccountResponse> getAll(Pageable pageable);

    AccountResponse getById(Long id);

    AccountResponse create(AccountRequest request);

    AccountResponse update(Long id, AccountRequest request);

    void delete(Long id);
}
