package com.valteris.database.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Table {

    private Long incrementor = 0L;
    private String tableName;
    private String dbName;
    private List<Column> columns;
    private List<Line> lines = new ArrayList<>();

}
