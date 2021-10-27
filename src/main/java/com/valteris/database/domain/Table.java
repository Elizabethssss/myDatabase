package com.valteris.database.domain;

import com.valteris.database.exception.ColumnNotFoundException;
import com.valteris.database.exception.LineNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Table {

    private Long incrementor = 1L;
    private String tableName;
    private String dbName;
    private List<Column> columns;
    private List<Line> lines = new ArrayList<>();

    //todo check list size
    public Column getUpdatedColumn() {
        return this.columns.get(0);
    }

    public void createColumn(Column column) {
        this.columns.add(column);
    }

    public Column getDeleteColumn(String columnName) {
        return this.columns.stream()
                .filter(c -> c.getName().equals(columnName))
                .findFirst()
                .orElseThrow(ColumnNotFoundException::new);
    }

    public Line getDeleteLine(Long id) {
        return this.lines.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(LineNotFoundException::new);
    }
}
