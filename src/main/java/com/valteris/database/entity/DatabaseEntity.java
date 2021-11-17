package com.valteris.database.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "db")
public class DatabaseEntity {

    @Id
    @Column(name = "id")
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "databaseEntity",
            cascade = CascadeType.ALL
    )
    private List<TableEntity> tableEntityList = new ArrayList<>();

    public void addTableEntity(TableEntity tableEntity) {
        tableEntityList.add(tableEntity);
        tableEntity.setDatabaseEntity(this);
    }

    public void removeTableEntity(TableEntity tableEntity) {
        tableEntityList.remove(tableEntity);
        tableEntity.setDatabaseEntity(null);
    }
}
