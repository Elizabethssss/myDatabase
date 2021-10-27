package com.valteris.database.service.impl;

import com.valteris.database.domain.Database;
import com.valteris.database.domain.Table;
import com.valteris.database.entity.DatabaseEntity;
import com.valteris.database.exception.DatabaseNotFoundException;
import com.valteris.database.repository.DatabaseRepository;
import com.valteris.database.service.DatabaseService;
import com.valteris.database.service.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Profile("mysql")
public class MySqlDatabaseServiceImpl implements DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final Mapper<Database, DatabaseEntity> databaseMapper;

    @Override
    public List<String> findAllDatabases() {
        return databaseRepository.findAll().stream()
                .map(DatabaseEntity::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Database findDatabaseByName(String name) {
        return databaseRepository.findDatabaseEntityByName(name)
                .map(databaseMapper::mapEntityToDomain)
                .orElseThrow(DatabaseNotFoundException::new);
    }

    @Override
    public void createDatabase(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createTable(Table table) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Table findTableByName(String dbName, String tableName) {
        throw new UnsupportedOperationException();
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
}
