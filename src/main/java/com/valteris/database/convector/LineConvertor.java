package com.valteris.database.convector;

import com.valteris.database.domain.Cell;
import com.valteris.database.domain.Column;
import com.valteris.database.domain.Line;
import com.valteris.database.dto.CellJson;
import com.valteris.database.dto.LineJson;
import com.valteris.database.entity.CellEntity;
import com.valteris.database.entity.ColumnEntity;
import com.valteris.database.entity.LineEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineConvertor {

    private final ModelMapper modelMapper;
    private final CellConvertor cellConvertor;

    public LineConvertor() {
        this.modelMapper = new ModelMapper();
        this.cellConvertor = new CellConvertor();
    }

    public Line convertToDomain(LineJson lineJson) {
        Line line = modelMapper.typeMap(LineJson.class, Line.LineBuilder.class)
                .addMapping(LineJson::getId, Line.LineBuilder::id)
                .map(lineJson)
                .build();

        List<Cell> cellList = lineJson.getCells().stream()
                .map(cellConvertor::convertToDomain)
                .collect(Collectors.toList());
        line.setCells(cellList);

        return line;
    }

    public Line convertEntityToDomain(LineEntity lineEntity) {
        Line line = modelMapper.typeMap(LineEntity.class, Line.LineBuilder.class)
                .addMapping(LineEntity::getId, Line.LineBuilder::id)
                .map(lineEntity)
                .build();

        List<Cell> cellList = lineEntity.getCellEntities().stream()
                .map(cellConvertor::convertEntityToDomain)
                .collect(Collectors.toList());
        line.setCells(cellList);

        return line;
    }

    public LineEntity convertToEntity(Line line) {
        LineEntity lineEntity = new LineEntity();

        List<CellEntity> cellEntityList = line.getCells().stream()
                .map(cellConvertor::convertToEntity)
                .collect(Collectors.toList());
        lineEntity.setCellEntities(cellEntityList);

        return lineEntity;
    }

    public LineJson convertToDTO(Line line) {
        return modelMapper.typeMap(Line.class, LineJson.class)
                .addMapping(Line::getId, LineJson::setId)
                .addMapping(l -> modelMapper.map(l.getCells(), CellJson.class), LineJson::setCells)
                .map(line);
    }
}
