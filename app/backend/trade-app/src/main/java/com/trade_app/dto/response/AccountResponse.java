package com.trade_app.dto.response;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long id;
    private String name;
    private String userInfo;
}
