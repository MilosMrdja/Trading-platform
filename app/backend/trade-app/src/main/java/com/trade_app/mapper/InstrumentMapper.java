package com.trade_app.mapper;

import com.trade_app.domain.entity.Instrument;
import com.trade_app.dto.request.InstrumentRequest;
import com.trade_app.dto.response.InstrumentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstrumentMapper {
    InstrumentResponse toResponse(Instrument instrument);

    //@Mapping(target = "code", source = "code")
    Instrument toEntity(InstrumentRequest req);
}
