package com.trade_app.service.interfaces;

import com.trade_app.common.shared.PageResponse;
import com.trade_app.dto.request.InstrumentRequest;
import com.trade_app.dto.response.InstrumentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstrumentService {
    PageResponse<InstrumentResponse> getAll(Pageable pageable);

    InstrumentResponse getById(Long id);

    InstrumentResponse create(InstrumentRequest request);

    InstrumentResponse update(Long id, InstrumentRequest request);

    void delete(Long id);
}
