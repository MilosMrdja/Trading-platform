package com.trade_app.web.controller;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.ExerciseRequest;
import com.trade_app.dto.request.TradeFilterRequest;
import com.trade_app.dto.request.TradeRequest;
import com.trade_app.dto.response.TradeResponse;
import com.trade_app.service.TradeServiceImpl;
import com.trade_app.service.interfaces.TradeService;
import jakarta.validation.Valid;
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
@RequestMapping("api/trades")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<TradeResponse>> getAllTrades(
            @ModelAttribute TradeFilterRequest filter,
            @PageableDefault(size = 8, page = 0, sort = {"price", "id"}, direction = Sort.Direction.DESC)
            Pageable pageable
            ) {
        return ResponseEntity.ok(tradeService.getAll(filter, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TradeResponse> getTrade(@PathVariable Long id) {
        return ResponseEntity.ok(tradeService.getTrade(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TradeResponse> createTrade(@Valid @RequestBody TradeRequest tradeRequest) {
        TradeResponse response = tradeService.createTrade(tradeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TradeResponse> updateTrade(@PathVariable Long id,
                                                     @Valid @RequestBody TradeRequest tradeRequest) {
        return ResponseEntity.ok(tradeService.updateTrade(id, tradeRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long id) {
        tradeService.deleteTrade(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/exercise")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TradeResponse> exerciseTrade(@PathVariable Long id, @RequestBody ExerciseRequest exerciseRequest) {
        return ResponseEntity.ok(tradeService.exerciseTrade(id, exerciseRequest));
    }

//    @PutMapping("/{id}/call")
//    @PreAuthorize("hasRole('BROKER')")
//    public ResponseEntity<TradeResponse> callTrade(@PathVariable Long id, @Valid @RequestBody ExerciseRequest tradeRequest) {
//        return ResponseEntity.ok(tradeService.callExercise(id, tradeRequest));
//    }
//
//    @PutMapping("/{id}/put")
//    @PreAuthorize("hasRole('BROKER')")
//    public ResponseEntity<TradeResponse> putTrade(@PathVariable Long id, @Valid @RequestBody ExerciseRequest tradeRequest) {
//        return ResponseEntity.ok(tradeService.putExercise(id, tradeRequest));
//    }

    @PutMapping("/{id}/close")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TradeResponse> closeTrade(@PathVariable Long id) {
        return ResponseEntity.ok(tradeService.closeTrade(id));
    }

}
