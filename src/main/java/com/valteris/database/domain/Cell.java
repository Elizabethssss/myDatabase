package com.valteris.database.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cell {

    private Long id;
    private String value = "";
    private Type type;
    private Line line;
    private Column column;
}
