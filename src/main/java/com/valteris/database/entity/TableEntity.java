package com.valteris.database.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl")
public class TableEntity {

    @Id
    @NonNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "db_name")
    private DatabaseEntity databaseEntity;

    @OneToMany(mappedBy = "tableEntity",
            cascade = CascadeType.ALL)
    private List<ColumnEntity> columnEntities = new ArrayList<>();

    @OneToMany(mappedBy = "tableEntity",
            cascade = CascadeType.ALL)
    private List<LineEntity> lineEntities = new ArrayList<>();

    public void addColumnEntity(ColumnEntity columnEntity) {
        columnEntities.add(columnEntity);
        columnEntity.setTableEntity(this);
    }

    public void removeColumnEntity(ColumnEntity columnEntity) {
        columnEntities.remove(columnEntity);
        columnEntity.setTableEntity(null);
    }

    public void addLineEntity(LineEntity lineEntity) {
        lineEntities.add(lineEntity);
        lineEntity.setTableEntity(this);
    }

    public void removeLineEntity(LineEntity lineEntity) {
        lineEntities.remove(lineEntity);
        lineEntity.setTableEntity(null);
    }
}
