package com.trade_app.service.interfaces;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.CreateAccountStatusRequest;
import com.trade_app.dto.response.AccountStatusResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AccountStatusService {

    PageResponse<AccountStatusResponse> getAll(Pageable pageable);

    AccountStatusResponse getAccountStatus(Long id);

    AccountStatusResponse createAccountStatus(CreateAccountStatusRequest request);

    AccountStatusResponse updateAccountStatus(Long id, CreateAccountStatusRequest request);

    List<AccountStatusResponse> synchronizeAccountStatuses(Map<Long, Double> OTEs);
}
