package com.valteris.database.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cell {

    private Long id;
    private String value;
    private Type type;
    private boolean hasError;
    private String columnName;
    private Long lineId;
}
