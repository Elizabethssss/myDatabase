package com.valteris.database.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Database {

    private Long id;
    private String name;
    private List<Table> tables = new ArrayList<>();
}
