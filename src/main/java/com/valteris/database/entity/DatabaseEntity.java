package com.valteris.database.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "db")
public class DatabaseEntity {

    @Id
    @Column(name = "name")
    @NonNull
    private String name;

    @OneToMany(mappedBy = "databaseEntity")
    private List<TableEntity> tableEntityList;
}
