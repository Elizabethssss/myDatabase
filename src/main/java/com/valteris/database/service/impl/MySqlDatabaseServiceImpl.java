package com.valteris.database.service.impl;

import com.valteris.database.convector.ColumnConvertor;
import com.valteris.database.convector.DatabaseConvertor;
import com.valteris.database.convector.TableConvertor;
import com.valteris.database.domain.Cell;
import com.valteris.database.domain.Column;
import com.valteris.database.domain.Database;
import com.valteris.database.domain.JoinResult;
import com.valteris.database.domain.Table;
import com.valteris.database.entity.CellEntity;
import com.valteris.database.entity.ColumnEntity;
import com.valteris.database.entity.DatabaseEntity;
import com.valteris.database.entity.JoinResultEntity;
import com.valteris.database.entity.LineEntity;
import com.valteris.database.entity.TableEntity;
import com.valteris.database.exception.ColumnNotFoundException;
import com.valteris.database.exception.DatabaseNotFoundException;
import com.valteris.database.exception.LineNotFoundException;
import com.valteris.database.exception.TableNotFoundException;
import com.valteris.database.repository.ColumnRepository;
import com.valteris.database.repository.DatabaseRepository;
import com.valteris.database.repository.LineRepository;
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
    private final ColumnRepository columnRepository;
    private final LineRepository lineRepository;

    private final DatabaseConvertor databaseConvertor;
    private final TableConvertor tableConvertor;
    private final ColumnConvertor columnConvertor;

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
        TableEntity tableEntity = tableRepository.findTableEntityByName(tableName)
                .orElseThrow(TableNotFoundException::new);

        LineEntity lineEntity = new LineEntity();

        for (ColumnEntity column : tableEntity.getColumnEntities()) {
            CellEntity cellEntity = new CellEntity();
            cellEntity.setType(column.getType());
            column.addCellEntity(cellEntity);
            lineEntity.addCellEntity(cellEntity);
        }

        tableEntity.addLineEntity(lineEntity);

        tableRepository.save(tableEntity);
    }

    @Override
    public void addNewColumn(Table table) {
        TableEntity tableEntity = tableRepository.findTableEntityByName(table.getTableName())
                .orElseThrow(TableNotFoundException::new);

        ColumnEntity columnEntity = columnConvertor.convertToEntity(table.getFirstColumn());

        for (LineEntity line : tableEntity.getLineEntities()) {
            CellEntity cellEntity = new CellEntity();
            cellEntity.setType(columnEntity.getType());
            columnEntity.addCellEntity(cellEntity);
            line.addCellEntity(cellEntity);
        }

        tableEntity.addColumnEntity(columnEntity);

        tableRepository.save(tableEntity);
    }

    @Override
    public void deleteColumn(Table table) {
        TableEntity tableEntity = tableRepository.findTableEntityByName(table.getTableName())
                .orElseThrow(TableNotFoundException::new);

        ColumnEntity columnEntity = getColumnEntityFromExactTable(table, tableEntity);

        tableEntity.removeColumnEntity(columnEntity);

        columnRepository.deleteById(columnEntity.getId());
    }

    @Override
    public void deleteLine(Long id, String dbName, String tableName) {
        lineRepository.deleteById(id);
    }

    @Override
    public void saveTable(Table table) {
        TableEntity tableEntity = tableRepository.findTableEntityByName(table.getTableName())
                .orElseThrow(TableNotFoundException::new);

        tableEntity.setCellEntityValues(table.getAllCells().stream()
                .map(Cell::getValue)
                .collect(Collectors.toList()));

        tableRepository.save(tableEntity);
    }

    @Override
    public Table innerJoin(String dbName, Table table1, Table table2) {
        TableEntity leftTableEntity = tableRepository.findTableEntityByName(table1.getTableName())
                .orElseThrow(TableNotFoundException::new);
        TableEntity rightTableEntity = tableRepository.findTableEntityByName(table2.getTableName())
                .orElseThrow(TableNotFoundException::new);

        ColumnEntity leftColumnEntity = getColumnEntityFromExactTable(table1, leftTableEntity);
        ColumnEntity rightColumnEntity = getColumnEntityFromExactTable(table2, rightTableEntity);
        List<CellEntity> leftCellEntities = leftColumnEntity.getCellEntities();
        List<CellEntity> rightCellEntities = rightColumnEntity.getCellEntities();

        JoinResultEntity.JoinResultEntityBuilder joinResultBuilder = JoinResultEntity.builder();

        for (CellEntity leftCell : leftCellEntities) {
            for (CellEntity rightCell : rightCellEntities) {
                if (leftCell.getValue().equals(rightCell.getValue())) {
                    LineEntity lineEntity = leftCell.getLineEntity();
                    lineEntity.getCellEntities().addAll(rightCell.getLineEntity().getCellEntities());
                    lineEntity.removeCellEntity(rightCell);
                    joinResultBuilder.resultLineEntity(lineEntity);
                }
            }
        }

        joinResultBuilder.resultColumnEntities(leftTableEntity.getColumnEntities());

        rightTableEntity.removeColumnEntity(rightColumnEntity);
        joinResultBuilder.resultColumnEntities(rightTableEntity.getColumnEntities());

        JoinResultEntity resultEntity = joinResultBuilder.build();

        TableEntity resultTable = new TableEntity();
        resultTable.setName("result");
        resultTable.setLineEntities(resultEntity.getResultLineEntities());
        resultTable.setColumnEntities(resultEntity.getResultColumnEntities());

        return tableConvertor.convertEntityToDomain(resultTable);
    }

    private ColumnEntity getColumnEntityFromExactTable(Table table, TableEntity tableEntity) {
        return tableEntity.getColumnEntities().stream()
                .filter(c -> c.getName().equals(table.getFirstColumn().getName()))
                .findFirst()
                .orElseThrow(ColumnNotFoundException::new);
    }
}
