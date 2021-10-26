package com.valteris.database.util;

import com.valteris.database.domain.Column;
import com.valteris.database.dto.ColumnJson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ColumnConvertor {

    private final ModelMapper modelMapper;

    public ColumnConvertor() {
        this.modelMapper = new ModelMapper();
    }

    public Column convertToDomain(ColumnJson columnJson) {
        return modelMapper.typeMap(ColumnJson.class, Column.class)
                .addMapping(ColumnJson::getName, Column::setName)
                .addMapping(ColumnJson::getType, Column::setType)
                .map(columnJson);
    }

    public ColumnJson convertToDTO(Column column) {
        return modelMapper.typeMap(Column.class, ColumnJson.class)
                .addMapping(Column::getName, ColumnJson::setName)
                .addMapping(Column::getType, ColumnJson::setType)
                .map(column);
    }
}
