package com.valteris.database.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Column {

    private Long id;
    private String name;
    private Type type;
    private Integer maxLength;
    private Table table;
    private List<Cell> cells;
}
