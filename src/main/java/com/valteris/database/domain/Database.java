package com.valteris.database.domain;

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
public class Database {

    private Long id;
    private String name;
    private List<TableEntity> tableEntityList;
}
