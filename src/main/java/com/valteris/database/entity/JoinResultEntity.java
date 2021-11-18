package com.valteris.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class JoinResultEntity {

    @Singular
    private List<LineEntity> resultLineEntities;

    @Singular
    private List<ColumnEntity> resultColumnEntities;
}
