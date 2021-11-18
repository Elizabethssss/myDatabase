package com.valteris.database.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Column {

    private Long id;
    private String name;
    private Type type;
    private Table table;
    private List<Cell> cells;
}
