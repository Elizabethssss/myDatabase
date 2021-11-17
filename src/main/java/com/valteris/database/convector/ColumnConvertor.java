package com.valteris.database.convector;

import com.valteris.database.domain.Column;
import com.valteris.database.domain.Table;
import com.valteris.database.dto.ColumnJson;
import com.valteris.database.entity.ColumnEntity;
import com.valteris.database.entity.TableEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ColumnConvertor {

    private final ModelMapper modelMapper;

    public ColumnConvertor() {
        this.modelMapper = new ModelMapper();
    }

    public Column convertToDomain(ColumnJson columnJson) {
        return modelMapper.typeMap(ColumnJson.class, Column.ColumnBuilder.class)
                .addMapping(ColumnJson::getName, Column.ColumnBuilder::name)
                .addMapping(ColumnJson::getType, Column.ColumnBuilder::type)
                .map(columnJson)
                .build();
    }

    public Column convertEntityToDomain(ColumnEntity tableEntity) {
        return modelMapper.typeMap(ColumnEntity.class, Column.ColumnBuilder.class)
                .addMapping(ColumnEntity::getId, Column.ColumnBuilder::id)
                .addMapping(ColumnEntity::getName, Column.ColumnBuilder::name)
                .addMapping(ColumnEntity::getType, Column.ColumnBuilder::type)
                .map(tableEntity)
                .build();
    }

    public ColumnEntity convertToEntity(Column column) {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName(column.getName());
        columnEntity.setType(column.getType());
        return columnEntity;
//        return modelMapper.typeMap(Column.class, ColumnEntity.class)
////                .addMapping(Column::getId, ColumnEntity::setId)
//                .addMapping(Column::getName, ColumnEntity::setName)
//                .addMapping(Column::getType, ColumnEntity::setType)
//                .map(column);
    }

    public ColumnJson convertToDTO(Column column) {
        return modelMapper.typeMap(Column.class, ColumnJson.class)
                .addMapping(Column::getName, ColumnJson::setName)
                .addMapping(Column::getType, ColumnJson::setType)
                .map(column);
    }
}
