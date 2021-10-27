package com.valteris.database.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
@Builder
public class Line {

    private Long id;
    private Table table;

    @Singular
    private List<Cell> cells;

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }
}
