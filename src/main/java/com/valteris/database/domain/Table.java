package com.valteris.database.domain;

import com.valteris.database.exception.ColumnNotFoundException;
import com.valteris.database.exception.LineNotFoundException;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class Table {

    private Long id;
    private Long incrementor;
    private String tableName;
    private String dbName;

    @Singular
    private List<Column> columns;

    @Singular
    private List<Line> lines;

    //todo check list size
    public Column getFirstColumn() {
        return this.columns.get(0);
    }

    public void addColumnToList(Column column) {
        this.columns.add(column);
    }

    public Column getColumnByName(String columnName) {
        return this.columns.stream()
                .filter(c -> c.getName().equals(columnName))
                .findFirst()
                .orElseThrow(ColumnNotFoundException::new);
    }

    public Line getLineById(Long id) {
        return this.lines.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(LineNotFoundException::new);
    }

    public List<Cell> getAllCells() {
        return lines.stream()
                .flatMap(line -> line.getCells().stream())
                .collect(Collectors.toList());
    }


    public List<Cell> getColumnCells(String columnName) {
        return lines.stream()
                .flatMap(line -> line.getCells().stream())
                .filter(c -> c.getColumnName().equals(columnName))
                .collect(Collectors.toList());
    }
}
