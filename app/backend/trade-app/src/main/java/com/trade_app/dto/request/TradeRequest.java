package com.trade_app.dto.request;

import com.trade_app.domain.enums.DeliveryType;
import com.trade_app.domain.enums.Direction;
import com.trade_app.domain.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {
    private Long instrumentId;
    private Long accountId;
    private Integer quantity;
    private Float price;
    private Direction direction;
    private DeliveryType deliveryType;
    private Unit unit;
}
