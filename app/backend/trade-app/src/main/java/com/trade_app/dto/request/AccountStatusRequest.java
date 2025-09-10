package com.trade_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatusRequest {
    private AccountRequest account;
    private Float ote;

}
