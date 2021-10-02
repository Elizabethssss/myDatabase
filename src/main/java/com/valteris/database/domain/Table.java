package com.valteris.database.domain;

import com.valteris.database.entity.ColumnEntity;
import com.valteris.database.entity.DatabaseEntity;
import com.valteris.database.entity.LineEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Table {

    private Long id;
    private String name;
    private DatabaseEntity databaseEntity;
    private List<ColumnEntity> columnEntities;
    private List<LineEntity> lineEntities;
}
