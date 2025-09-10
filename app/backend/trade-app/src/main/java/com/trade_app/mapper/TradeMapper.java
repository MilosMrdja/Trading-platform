package com.trade_app.mapper;

import com.trade_app.domain.entity.Trade;
import com.trade_app.dto.request.TradeRequest;
import com.trade_app.dto.response.TradeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TradeMapper {
    Trade toEntity(TradeRequest tradeRequest);
    //@Mapping(source = "price", target = "price")
    TradeResponse toResponse(Trade trade);
}
