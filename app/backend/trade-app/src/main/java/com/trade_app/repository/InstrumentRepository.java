package com.trade_app.repository;

import com.trade_app.domain.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {


    boolean existsInstrumentByCode(Long code);
}
