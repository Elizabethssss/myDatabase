package com.valteris.database.convector;

import com.valteris.database.domain.Cell;
import com.valteris.database.domain.Line;
import com.valteris.database.dto.CellJson;
import com.valteris.database.entity.CellEntity;
import com.valteris.database.entity.LineEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CellConvertor {

    private final ModelMapper modelMapper;

    public CellConvertor() {
        this.modelMapper = new ModelMapper();
    }

    public Cell convertToDomain(CellJson columnJson) {
        return modelMapper.typeMap(CellJson.class, Cell.CellBuilder.class)
                .addMapping(CellJson::getValue, Cell.CellBuilder::value)
                .addMapping(CellJson::getType, Cell.CellBuilder::type)
                .addMapping(CellJson::isHasError, Cell.CellBuilder::hasError)
                .addMapping(CellJson::getColumnName, Cell.CellBuilder::columnName)
                .addMapping(CellJson::getLineId, Cell.CellBuilder::lineId)
                .map(columnJson)
                .build();
    }

    public Cell convertEntityToDomain(CellEntity cellEntity) {
        Cell.CellBuilder cellBuilder = modelMapper.typeMap(CellEntity.class, Cell.CellBuilder.class)
                .addMapping(CellEntity::getId, Cell.CellBuilder::id)
                .addMapping(CellEntity::getValue, Cell.CellBuilder::value)
                .addMapping(CellEntity::getType, Cell.CellBuilder::type)
                .map(cellEntity);

        cellBuilder
                .lineId(cellEntity.getLineEntity().getId())
                .columnName(cellEntity.getColumnEntity().getName());

        return cellBuilder.build();
    }

    public CellEntity convertToEntity(Cell cell) {
        return modelMapper.typeMap(Cell.class, CellEntity.class)
//                .addMapping(Cell::getId, CellEntity::setId)
                .addMapping(Cell::getValue, CellEntity::setValue)
//                .addMapping(Cell::getLineId, CellEntity::se)
//                .addMapping(Cell::getColumnName, CellEntity::columnName)
                .map(cell);
    }

    public CellJson convertToDTO(Cell column) {
        return modelMapper.typeMap(Cell.class, CellJson.class)
                .addMapping(Cell::getValue, CellJson::setValue)
                .addMapping(Cell::getType, CellJson::setType)
                .addMapping(Cell::isHasError, CellJson::setHasError)
                .addMapping(Cell::getColumnName, CellJson::setColumnName)
                .addMapping(Cell::getLineId, CellJson::setLineId)
                .map(column);
    }
}
