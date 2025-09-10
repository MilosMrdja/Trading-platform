package com.trade_app.service;

import com.trade_app.common.exception.NotFoundError;
import com.trade_app.common.shared.PageResponse;
import com.trade_app.domain.entity.Account;
import com.trade_app.domain.entity.AccountStatus;
import com.trade_app.dto.request.CreateAccountStatusRequest;
import com.trade_app.dto.response.AccountStatusResponse;
import com.trade_app.mapper.AccountStatusMapper;
import com.trade_app.repository.AccountRepository;
import com.trade_app.repository.AccountStatusRepository;
import com.trade_app.service.interfaces.AccountService;
import com.trade_app.service.interfaces.AccountStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountStatusServiceImpl implements AccountStatusService {
    private final AccountStatusRepository accountStatusRepository;
    private final AccountStatusMapper accountStatusMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Override
    public PageResponse<AccountStatusResponse> getAll(Pageable pageable) {
        Page<AccountStatus> page = accountStatusRepository.findAll(pageable);

        List<AccountStatusResponse> content = page.getContent()
                .stream()
                .map(accountStatusMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public AccountStatusResponse getAccountStatus(Long id){
        AccountStatus accountStatus = accountStatusRepository.findById(id).orElseThrow(()-> new NotFoundError("Account status not found"));
        return accountStatusMapper.toResponse(accountStatus);
    }

    @Override
    public AccountStatusResponse createAccountStatus(CreateAccountStatusRequest request){
        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(()-> new NotFoundError("Account not found"));
        AccountStatus accountStatus = AccountStatus.builder()
                .account(account)
                .ote(request.getOte())
                .build();
        return accountStatusMapper.toResponse(accountStatusRepository.save(accountStatus));
    }

    @Override
    public AccountStatusResponse updateAccountStatus(Long id, CreateAccountStatusRequest request){
        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(()-> new NotFoundError("Account not found"));
        AccountStatus accountStatus = accountStatusRepository.findById(id).orElseThrow(()-> new NotFoundError("Account status not found"));
        accountStatus.setOte(request.getOte());
        return accountStatusMapper.toResponse(accountStatusRepository.save(accountStatus));
    }

    @Override
    // param: Map<key = accountId, value = OTE>
    // param type: Map<key, value>
    public List<AccountStatusResponse> synchronizeAccountStatuses(Map<Long, Double> OTEs){
        List<AccountStatusResponse> res = new ArrayList<>();
        AccountStatusResponse acc;
        for (Map.Entry<Long, Double> entry : OTEs.entrySet()) {
            Long accountId = entry.getKey();
            Double oteValue = entry.getValue();

            Optional<AccountStatus> accountStatus = accountStatusRepository.findByAccountId(accountId);
            if(accountStatus.isPresent()){
                 acc = updateAccountStatus(accountStatus.get().getId(), new CreateAccountStatusRequest(accountId, oteValue));
            }else {
                acc = createAccountStatus(new CreateAccountStatusRequest(accountId, oteValue));
            }
            res.add(acc);
        }

        return res;
    }

}
