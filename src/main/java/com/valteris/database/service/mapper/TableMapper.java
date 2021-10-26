package com.valteris.database.service.mapper;

import com.valteris.database.domain.Table;
import com.valteris.database.entity.TableEntity;
import org.springframework.stereotype.Component;

@Component
public class TableMapper implements Mapper<Table, TableEntity> {
    @Override
    public Table mapEntityToDomain(TableEntity entity) {
        return null;
    }

    @Override
    public TableEntity mapDomainToEntity(Table domain) {
        return null;
    }
}
