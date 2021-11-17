package com.valteris.database.dto;

import com.valteris.database.domain.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableErrorValidation {

    private Table table;
    private boolean hasError;
}
