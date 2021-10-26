package com.valteris.database.service.mapper;

import com.valteris.database.domain.Column;
import com.valteris.database.entity.ColumnEntity;
import org.springframework.stereotype.Component;

@Component
public class ColumnMapper implements Mapper<Column, ColumnEntity> {
    @Override
    public Column mapEntityToDomain(ColumnEntity entity) {
        return null;
    }

    @Override
    public ColumnEntity mapDomainToEntity(Column domain) {
        return null;
    }
}
