package com.trade_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatusResponse {
    private Long id;
    private AccountResponse account;
    private Float ote;
}
