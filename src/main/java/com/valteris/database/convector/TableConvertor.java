package com.valteris.database.convector;

import com.valteris.database.domain.Column;
import com.valteris.database.domain.Line;
import com.valteris.database.domain.Table;
import com.valteris.database.dto.ColumnJson;
import com.valteris.database.dto.CreateTableJson;
import com.valteris.database.dto.TableJoinJson;
import com.valteris.database.dto.TableJson;
import com.valteris.database.entity.ColumnEntity;
import com.valteris.database.entity.LineEntity;
import com.valteris.database.entity.TableEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableConvertor {

    private final ModelMapper modelMapper;
    private final ColumnConvertor columnConvertor;
    private final LineConvertor lineConvertor;

    public TableConvertor() {
        this.modelMapper = new ModelMapper();
        this.columnConvertor = new ColumnConvertor();
        this.lineConvertor = new LineConvertor();
    }

    public Table convertCreateTableToDomain(CreateTableJson tableJson) {
        Table table = modelMapper.typeMap(CreateTableJson.class, Table.TableBuilder.class)
                .addMapping(CreateTableJson::getTableName, Table.TableBuilder::tableName)
                .addMapping(CreateTableJson::getDbName, Table.TableBuilder::dbName)
                .map(tableJson)
                .build();

        List<Column> columnList = tableJson.getColumns().stream()
                .map(columnConvertor::convertToDomain)
                .collect(Collectors.toList());
        table.setColumns(columnList);

        return table;
    }

    public Table convertToDomain(TableJson tableJson) {
        Table table = modelMapper.typeMap(TableJson.class, Table.TableBuilder.class)
                .addMapping(TableJson::getTableName, Table.TableBuilder::tableName)
                .addMapping(TableJson::getDbName, Table.TableBuilder::dbName)
                .map(tableJson)
                .build();

        List<Column> columnList = tableJson.getColumns().stream()
                .map(columnConvertor::convertToDomain)
                .collect(Collectors.toList());
        table.setColumns(columnList);
        List<Line> lineList = tableJson.getLines().stream()
                .map(lineConvertor::convertToDomain)
                .collect(Collectors.toList());
        table.setLines(lineList);

        return table;
    }

    public Table convertEntityToDomain(TableEntity tableEntity) {
        Table table = modelMapper.typeMap(TableEntity.class, Table.TableBuilder.class)
                .addMapping(TableEntity::getId, Table.TableBuilder::id)
                .addMapping(TableEntity::getName, Table.TableBuilder::tableName)
                .map(tableEntity)
                .build();

        table.setDbName(tableEntity.getDatabaseEntity().getName());

        List<Column> columnList = tableEntity.getColumnEntities().stream()
                .map(columnConvertor::convertEntityToDomain)
                .collect(Collectors.toList());
        table.setColumns(columnList);

        List<Line> lineList = tableEntity.getLineEntities().stream()
                .map(lineConvertor::convertEntityToDomain)
                .collect(Collectors.toList());
        table.setLines(lineList);

        return table;
    }

    public TableEntity convertToEntity(Table table) {
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName(table.getTableName());

        table.getColumns().stream()
                .map(columnConvertor::convertToEntity)
                .forEach(tableEntity::addColumnEntity);

        table.getLines().stream()
                .map(lineConvertor::convertToEntity)
                .forEach(tableEntity::addLineEntity);

        return tableEntity;
    }

    public Table convertJoinTableToDomain(TableJoinJson tableJson) {
        Table table = modelMapper.typeMap(TableJoinJson.class, Table.TableBuilder.class)
                .addMapping(TableJoinJson::getTableName, Table.TableBuilder::tableName)
                .map(tableJson)
                .build();

        Column column = columnConvertor.convertToDomain(tableJson.getColumn());
        table.setColumns(new ArrayList<>());
        table.addColumnToList(column);

        return table;
    }

    public CreateTableJson convertToDTO(Table table) {
        return modelMapper.typeMap(Table.class, CreateTableJson.class)
                .addMapping(Table::getTableName, CreateTableJson::setTableName)
                .addMapping(Table::getDbName, CreateTableJson::setDbName)
                .addMapping(t -> modelMapper.map(t.getColumns(), ColumnJson.class), CreateTableJson::setColumns)
                .map(table);
    }

    public TableJson convertToJsonDTO(Table table) {
        return modelMapper.typeMap(Table.class, TableJson.class)
                .addMapping(Table::getTableName, TableJson::setTableName)
                .addMapping(Table::getDbName, TableJson::setDbName)
                .addMapping(t -> modelMapper.map(t.getColumns(), ColumnJson.class), TableJson::setColumns)
                .map(table);
    }

}
