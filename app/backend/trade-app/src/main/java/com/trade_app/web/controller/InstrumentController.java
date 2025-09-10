package com.trade_app.web.controller;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.InstrumentRequest;
import com.trade_app.dto.response.InstrumentResponse;
import com.trade_app.service.InstrumentServiceImpl;
import com.trade_app.service.interfaces.InstrumentService;
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
@RequestMapping("api/instruments")
@RequiredArgsConstructor
public class InstrumentController {
    private final InstrumentService instrumentService;


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<InstrumentResponse>> getAll(
            @PageableDefault(size = 20, page = 0, sort = {"maturityDate", "id"}, direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        return ResponseEntity.ok(instrumentService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InstrumentResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(instrumentService.getById(id));
    }
    @PostMapping()
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<InstrumentResponse> create(@RequestBody InstrumentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(instrumentService.create(request));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<InstrumentResponse> update(@PathVariable Long id, @RequestBody InstrumentRequest request){
        return ResponseEntity.ok(instrumentService.update(id, request));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        instrumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
