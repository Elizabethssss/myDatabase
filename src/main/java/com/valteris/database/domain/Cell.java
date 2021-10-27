package com.valteris.database.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Cell {

    private Long id;
    private String value = "";
    private Type type;
    private Line line;
    private Column column;
}
