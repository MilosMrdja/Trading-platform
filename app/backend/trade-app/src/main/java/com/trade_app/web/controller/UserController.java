package com.trade_app.web.controller;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.UserRequest;
import com.trade_app.dto.response.UserResponse;
import com.trade_app.service.UserServiceImpl;
import com.trade_app.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @PageableDefault(size = 20, page = 0, sort = "name", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @PostMapping("/broker")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserResponse> createBroker(@RequestBody UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.createBroker(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserResponse> createManager(@RequestBody UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.createManager(userRequest), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.updateUser(id, userRequest), HttpStatus.OK);
    }
}
