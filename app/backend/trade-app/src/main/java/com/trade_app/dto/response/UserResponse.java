package com.trade_app.dto.response;

import com.trade_app.domain.enums.Role;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
