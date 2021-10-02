package com.valteris.database.domain;

import com.valteris.database.entity.CellEntity;
import com.valteris.database.entity.TableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Line {

    private Long id;
    private TableEntity tableEntity;
    private List<CellEntity> cellEntities;
}
