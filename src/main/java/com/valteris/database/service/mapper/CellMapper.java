package com.valteris.database.service.mapper;

import com.valteris.database.domain.Cell;
import com.valteris.database.entity.CellEntity;
import org.springframework.stereotype.Component;

@Component
public class CellMapper implements Mapper<Cell, CellEntity> {
    @Override
    public Cell mapEntityToDomain(CellEntity entity) {
        return null;
    }

    @Override
    public CellEntity mapDomainToEntity(Cell domain) {
        return null;
    }
}
