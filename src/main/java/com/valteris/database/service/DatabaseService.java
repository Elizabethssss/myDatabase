package com.valteris.database.service;

import com.valteris.database.domain.Database;
import com.valteris.database.domain.Table;

import java.util.List;

//todo separate into 2 interfaces
public interface DatabaseService {

    List<String> findAllDatabaseNames();

    Database findDatabaseByName(String name);

    void createDatabase(String name);

    void createTable(Table table);

    Table findTableByName(String dbName, String tableName);

    void addNewLine(String dbName, String tableName);

    void addNewColumn(Table table);

    void deleteColumn(Table table);

    void deleteLine(Long id, String dbName, String tableName);

    void saveTable(Table table);

    Table innerJoin(String dbName, Table table1, Table table2);
}
