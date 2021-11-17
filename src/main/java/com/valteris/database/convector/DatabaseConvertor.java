package com.valteris.database.convector;

import com.valteris.database.domain.Database;
import com.valteris.database.domain.Table;
import com.valteris.database.entity.DatabaseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseConvertor {

    private final ModelMapper modelMapper;
    private final TableConvertor tableConvertor;

    public DatabaseConvertor() {
        this.modelMapper = new ModelMapper();
        this.tableConvertor = new TableConvertor();
    }

    public Database convertToDomain(DatabaseEntity databaseEntity) {
        Database database = modelMapper.typeMap(DatabaseEntity.class, Database.DatabaseBuilder.class)
                .addMapping(DatabaseEntity::getId, Database.DatabaseBuilder::id)
                .addMapping(DatabaseEntity::getName, Database.DatabaseBuilder::name)
                .map(databaseEntity)
                .build();

        List<Table> tables = databaseEntity.getTableEntityList().stream()
                .map(tableConvertor::convertEntityToDomain)
                .collect(Collectors.toList());
        database.setTables(tables);

        return database;
    }

    public DatabaseEntity convertToEntity(Database database) {
        return modelMapper.typeMap(Database.class, DatabaseEntity.class)
                .addMapping(Database::getId, DatabaseEntity::setId)
                .addMapping(Database::getName, DatabaseEntity::setName)
                .map(database);
    }
}
