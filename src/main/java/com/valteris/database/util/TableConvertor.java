package com.valteris.database.util;

import com.valteris.database.domain.Column;
import com.valteris.database.domain.Table;
import com.valteris.database.dto.ColumnJson;
import com.valteris.database.dto.CreateTableJson;
import com.valteris.database.dto.TableJson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TableConvertor {

    private final ModelMapper modelMapper;

    public TableConvertor() {
        this.modelMapper = new ModelMapper();
    }

    public Table convertToDomain(CreateTableJson tableJson) {
        return modelMapper.typeMap(CreateTableJson.class, Table.class)
                .addMapping(CreateTableJson::getTableName, Table::setTableName)
                .addMapping(CreateTableJson::getDbName, Table::setDbName)
                .addMapping(createTableJson -> modelMapper.map(createTableJson.getColumns(), Column.class), Table::setColumns)
                .map(tableJson);
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
