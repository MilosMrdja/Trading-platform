package com.trade_app.specifications;

import com.trade_app.domain.entity.Trade;
import com.trade_app.domain.enums.Direction;
import com.trade_app.domain.enums.Status;
import com.trade_app.dto.request.TradeFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TradeSpecification {
    public static Specification<Trade> filter(TradeFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), String.valueOf(filter.getStatus()))
                );
            }


            if (filter.getDirection() != null) {
                predicates.add(
                        cb.equal(root.get("direction"), filter.getDirection()));

            }


            if (filter.getPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getPrice()));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
