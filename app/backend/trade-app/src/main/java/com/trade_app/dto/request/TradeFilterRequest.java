package com.trade_app.dto.request;

import com.trade_app.domain.enums.Direction;
import com.trade_app.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeFilterRequest {
    private Float price;
    private Direction direction;
    private Status status;
}
