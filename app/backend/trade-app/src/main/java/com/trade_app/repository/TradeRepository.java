package com.trade_app.repository;

import com.trade_app.domain.entity.Account;
import com.trade_app.domain.entity.Instrument;
import com.trade_app.domain.entity.Trade;
import com.trade_app.domain.enums.DeliveryType;
import com.trade_app.domain.enums.Direction;
import com.trade_app.domain.enums.Status;
import com.trade_app.domain.enums.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long>, JpaSpecificationExecutor<Trade> {
    List<Trade> findAllByStatus(Status status);

    @Modifying
    @Transactional
    @Query("UPDATE Trade t SET t.status = 'CLOSED' " +
            "WHERE t.instrument.maturityDate < CURRENT_DATE " +
            "AND t.status <> 'CLOSED'")
    int closeExpiredTrades();

    @Query("SELECT t FROM Trade t WHERE t.price = :price AND t.quantity = :quantity AND t.instrument.id = :instrumentId " +
            "AND (t.status = 'OPEN') AND t.direction <> :direction " +
            "AND t.deliveryType = :deliveryType AND t.unit = :unit AND t.account.id <> :accountId " +
            "ORDER BY t.id ASC")
    List<Trade> findFirstMatchingTrade(
            @Param("price") Float price,
            @Param("quantity") Integer quantity,
            @Param("instrumentId") Long instrumentId,
            @Param("direction") Direction direction,
            @Param("deliveryType") DeliveryType deliveryType,
            @Param("unit") Unit unit,
            @Param("accountId") Long accountId
    );

    @Query("SELECT t FROM Trade t WHERE t.price = :price AND t.quantity = :quantity AND t.instrument.id = :instrumentId AND t.direction <> :direction AND t.deliveryType = :deliveryType AND t.unit = :unit AND " +
            "t.account.id <> :accountId AND t.status = 'MATCHED'")
    List<Trade> findMatchedTrade(@Param("price") Float price,
                                     @Param("quantity") Integer quantity,
                                     @Param("instrumentId") Long instrumentId,
                                     @Param("direction") Direction direction,
                                     @Param("deliveryType") DeliveryType deliveryType,
                                     @Param("unit") Unit unit,
                                     @Param("accountId") Long accountId);

    boolean existsByInstrument(Instrument instrument);
    boolean existsByAccount(Account account);

    List<Trade> getTradesByDirection(Direction direction);

    @Transactional
    @Modifying
    @Query("UPDATE Trade t SET t.status = 'OPEN' " +
            "WHERE t.status = 'MATCHED' " +
            "AND t.price = :price " +
            "AND t.quantity = :quantity " +
            "AND t.instrument.id = :instrumentId " +
            "AND t.deliveryType = :deliveryType " +
            "AND t.unit = :unit")
    int reopenMatchingTrades(
            @Param("price") Float price,
            @Param("quantity") Integer quantity,
            @Param("instrumentId") Long instrumentId,
            @Param("deliveryType") DeliveryType deliveryType,
            @Param("unit") Unit unit
    );

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Trade t " +
            "WHERE t.status = 'MATCHED' " +
            "AND t.price = :price " +
            "AND t.quantity = :quantity " +
            "AND t.direction = :direction " +
            "AND t.deliveryType = :deliveryType " +
            "AND t.unit = :unit AND t.instrument.id = :instrumentId AND t.account.id <> :accountId")
    boolean existsMatchingTrade(
            @Param("price") Float price,
            @Param("quantity") Integer quantity,
            @Param("direction") Direction direction,
            @Param("deliveryType") DeliveryType deliveryType,
            @Param("unit") Unit unit,
            @Param("instrumentId") Long instrumentId,
            @Param("accountId") Long accountId
    );
}
