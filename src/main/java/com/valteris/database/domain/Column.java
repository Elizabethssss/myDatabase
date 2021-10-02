package com.valteris.database.domain;

import com.valteris.database.entity.CellEntity;
import com.valteris.database.entity.TableEntity;
import com.valteris.database.entity.TypeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Column {

    private Long id;
    private String name;
    private TypeEntity typeEntity;
    private Integer maxLength;
    private TableEntity tableEntity;
    private List<CellEntity> cellEntities;
}
