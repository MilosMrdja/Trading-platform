package com.trade_app.dto.response;

import com.trade_app.domain.enums.DeliveryType;
import com.trade_app.domain.enums.Direction;
import com.trade_app.domain.enums.Status;
import com.trade_app.domain.enums.Unit;
import com.trade_app.dto.request.AccountRequest;
import com.trade_app.dto.request.InstrumentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeResponse {
    private Long id;
    private InstrumentResponse instrument;
    private AccountResponse account;
    private Integer quantity;
    private Float price;
    private Direction direction;
    private DeliveryType deliveryType;
    private Unit unit;
    private Status status;
}
