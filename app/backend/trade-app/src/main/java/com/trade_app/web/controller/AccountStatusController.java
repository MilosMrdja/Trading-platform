package com.trade_app.web.controller;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.CreateAccountStatusRequest;
import com.trade_app.dto.response.AccountStatusResponse;
import com.trade_app.kafka.producers.TradeEventProducer;
import com.trade_app.service.AccountStatusServiceImpl;
import com.trade_app.service.interfaces.AccountStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/account-status")
public class AccountStatusController {
    private final AccountStatusService accountStatusService;
    private final TradeEventProducer tradeEventProducer;

    @PostMapping("/kafka")
    public ResponseEntity<Boolean> updateAccountStatus(@RequestBody CreateAccountStatusRequest request) {
        return ResponseEntity.ok(tradeEventProducer.sendTrade());
    }

    @GetMapping
    public ResponseEntity<PageResponse<AccountStatusResponse>> getAll(
            @PageableDefault(size = 20, page = 0, sort = {"account", "id"}, direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(accountStatusService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountStatusResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountStatusService.getAccountStatus(id));
    }

    @PostMapping
    public ResponseEntity<AccountStatusResponse> create(@RequestBody CreateAccountStatusRequest request) {
        return new ResponseEntity<>(accountStatusService.createAccountStatus(request), HttpStatus.CREATED);
    }
}
