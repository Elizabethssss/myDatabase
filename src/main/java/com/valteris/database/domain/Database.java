package com.valteris.database.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Database {

    private Long id;
    private String name;

    @Singular
    private List<Table> tables;

    public void registerTable(Table table) {
        this.tables.add(table);
    }
}
