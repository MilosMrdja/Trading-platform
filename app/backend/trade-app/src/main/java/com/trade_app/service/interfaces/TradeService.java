package com.trade_app.service.interfaces;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.domain.entity.Trade;
import com.trade_app.dto.request.ExerciseRequest;
import com.trade_app.dto.request.TradeFilterRequest;
import com.trade_app.dto.request.TradeRequest;
import com.trade_app.dto.response.TradeResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeService {
    PageResponse<TradeResponse> getAll(TradeFilterRequest request, Pageable pageable);

    TradeResponse getTrade(Long id);

    TradeResponse createTrade(TradeRequest tradeRequest);

    TradeResponse updateTrade(Long id, TradeRequest tradeRequest);

    TradeResponse matchingTrade(Trade trade);
    void deleteTrade(Long id);

    Trade callExercise(Trade trade);

    Trade putExercise(Trade trade);

    TradeResponse closeTrade(Long id);

    List<Trade> fetchOpenTrades();

    TradeResponse exerciseTrade(Long id, ExerciseRequest request);
    void matchingAlgorithmByTrade(Trade trade);
}
