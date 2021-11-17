package com.valteris.database.service.impl;

import com.valteris.database.convector.DatabaseConvertor;
import com.valteris.database.convector.TableConvertor;
import com.valteris.database.domain.Cell;
import com.valteris.database.domain.Column;
import com.valteris.database.domain.Database;
import com.valteris.database.domain.Table;
import com.valteris.database.entity.CellEntity;
import com.valteris.database.entity.ColumnEntity;
import com.valteris.database.entity.DatabaseEntity;
import com.valteris.database.entity.TableEntity;
import com.valteris.database.exception.DatabaseNotFoundException;
import com.valteris.database.exception.TableNotFoundException;
import com.valteris.database.repository.DatabaseRepository;
import com.valteris.database.repository.TableRepository;
import com.valteris.database.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Profile("mysql")
public class MySqlDatabaseServiceImpl implements DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final TableRepository tableRepository;

    private final DatabaseConvertor databaseConvertor;
    private final TableConvertor tableConvertor;

    @Override
    public List<String> findAllDatabaseNames() {
        return databaseRepository.findAll().stream()
                .map(DatabaseEntity::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Database findDatabaseByName(String name) {
        return databaseRepository.findDatabaseEntityByName(name)
                .map(databaseConvertor::convertToDomain)
                .orElseThrow(DatabaseNotFoundException::new);
    }

    @Override
    public void createDatabase(String name) {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setName(name);

        databaseRepository.save(databaseEntity);
    }

    @Override
    public void createTable(Table table) {
        DatabaseEntity databaseEntity = databaseRepository.findDatabaseEntityByName(table.getDbName())
                .orElseThrow(DatabaseNotFoundException::new);
        databaseEntity.addTableEntity(tableConvertor.convertToEntity(table));

        databaseRepository.save(databaseEntity);
    }

    @Override
    public Table findTableByName(String dbName, String tableName) {
        return tableRepository.findTableEntityByName(tableName)
                .map(tableConvertor::convertEntityToDomain)
                .orElseThrow(TableNotFoundException::new);
    }

    @Override
    public void addNewLine(String dbName, String tableName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addNewColumn(Table table) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteColumn(Table table) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteLine(Long id, String dbName, String tableName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveTable(Table table) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Table innerJoin(String dbName, Table table1, Table table2) {
        throw new UnsupportedOperationException();
    }
}
