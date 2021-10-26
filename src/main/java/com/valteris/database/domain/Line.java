package com.valteris.database.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Line {

    private Long id;
    private Table table;
    private List<Cell> cells = new ArrayList<>();
}
