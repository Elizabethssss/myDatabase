package com.valteris.database.service;

import com.valteris.database.domain.Database;
import com.valteris.database.domain.Table;

import java.util.List;
import java.util.Optional;

public interface DatabaseService {

    List<String> findAllDatabases();

    Optional<Database> findDatabaseByName(String name);

    void createDatabase(String name);

    void createTable(Table table);

    Optional<Table> findTableByName(String dbName, String tableName);

    void addNewLine(String dbName, String tableName);

    void addNewColumn(Table table);

    void deleteColumn(Table table);

    void deleteLine(Long id, String dbName, String tableName);
}
