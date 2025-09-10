package com.trade_app.dto.request;

import com.trade_app.domain.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequest {
    private Long instrumentId;
    private Direction direction;
}
