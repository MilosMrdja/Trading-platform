package com.trade_app.service;

import com.trade_app.common.exception.BadRequestError;
import com.trade_app.common.exception.NotFoundError;
import com.trade_app.common.shared.PageResponse;
import com.trade_app.domain.entity.Account;
import com.trade_app.dto.request.AccountRequest;
import com.trade_app.dto.response.AccountResponse;
import com.trade_app.mapper.AccountMapper;
import com.trade_app.repository.AccountRepository;
import com.trade_app.repository.TradeRepository;
import com.trade_app.service.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final TradeRepository tradeRepository;


    @Override
    public PageResponse<AccountResponse> getAll(Pageable pageable) {
        Page<Account> page = accountRepository.findAll(pageable);

        List<AccountResponse> content = page.getContent()
                .stream()
                .map(accountMapper::toResponse)
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
    public AccountResponse getById(Long id){
        return accountRepository.findById(id).map(accountMapper::toResponse).orElseThrow(() -> new NotFoundError("Account not found"));
    }

    @Override
    public AccountResponse create(AccountRequest request){
        if(request.getName() == null){throw new BadRequestError("Account name is required");}
        if(request.getName().length()<2){throw new BadRequestError("Account name is too short");}

        Account account = accountMapper.toEntity(request);

        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse update(Long id, AccountRequest request){
        Account account = accountRepository.findById(id).orElseThrow(() -> new NotFoundError("Account not found"));

        account.setName(request.getName() == null ? account.getName() : request.getName());
        account.setUserInfo(request.getUserInfo() == null ? account.getUserInfo() : request.getUserInfo());
        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    public void delete(Long id){
        Account account = accountRepository.findById(id).orElseThrow(() -> new NotFoundError("Account not found"));
        if(tradeRepository.existsByAccount(account)){throw new NotFoundError("Can not delete the account because it is existed in some trade");}
        accountRepository.delete(account);
    }
}
