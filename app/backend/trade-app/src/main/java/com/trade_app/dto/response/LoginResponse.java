package com.trade_app.dto.response;

import lombok.*;
import lombok.experimental.Accessors;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class LoginResponse {
    private String token;

    private long expiresIn;

}
