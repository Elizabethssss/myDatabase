package com.valteris.database.domain;

import com.valteris.database.entity.ColumnEntity;
import com.valteris.database.entity.LineEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Cell {

    private Long id;
    private String value;
    private LineEntity lineEntity;
    private ColumnEntity columnEntity;
}
