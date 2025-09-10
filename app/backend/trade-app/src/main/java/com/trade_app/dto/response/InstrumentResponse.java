package com.trade_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentResponse {
    private Long id;
    private Long code;
    private LocalDateTime maturityDate;
}
