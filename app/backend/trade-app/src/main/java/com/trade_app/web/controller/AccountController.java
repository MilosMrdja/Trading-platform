package com.trade_app.web.controller;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.AccountRequest;
import com.trade_app.dto.response.AccountResponse;
import com.trade_app.service.AccountServiceImpl;
import com.trade_app.service.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<AccountResponse>> getAll(
            @PageableDefault(size = 20, page = 0, sort = {"name", "id"}, direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        return ResponseEntity.ok(accountService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<AccountResponse> create(@RequestBody AccountRequest accountRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(accountRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<AccountResponse> update(@PathVariable Long id, @RequestBody AccountRequest accountRequest){
        return ResponseEntity.ok(accountService.update(id, accountRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> delete(@PathVariable Long id){
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
