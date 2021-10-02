package com.valteris.database.service.mapper;

import com.valteris.database.domain.Database;
import com.valteris.database.entity.DatabaseEntity;

public class DatabaseMapper implements Mapper<Database, DatabaseEntity> {
    @Override
    public Database mapEntityToDomain(DatabaseEntity entity) {
        return null;
    }

    @Override
    public DatabaseEntity mapDomainToEntity(Database domain) {
        return null;
    }
}
